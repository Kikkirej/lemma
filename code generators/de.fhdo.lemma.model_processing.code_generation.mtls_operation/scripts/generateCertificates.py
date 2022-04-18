import os
import subprocess
import logging
import sys
import traceback
from dataclasses import dataclass
from enum import Enum
from shutil import which
from typing import List

target_path = os.path.expanduser("~/masterthesis/lemmasecuritymodelgencode")


# todo target_path as commandline arg

class ConfigurationException(Exception):
    pass


class ProfileType(Enum):
    MTLS = "mtls"
    MTLSDEV = "mtlsdev"


# todo check path for windows os
def get_absolute_path(path: str) -> str:
    if path.startswith("~"):
        return os.path.expanduser(path)
    return path


def check_empty_sys_env_var(variable: str) -> str:
    if variable.find("${") > -1:
        var = variable[variable.find("${") + 2:variable.find("}")]
        if not os.getenv(var):
            throw_configuration_exception("The system environment variable \"{var}\" is not defined!".format(var=var))
    return variable


@dataclass
class CertificateConfigFile:
    """Class for keeping the certification parameter"""
    rootDir: str
    filename: str
    configParameter: List[tuple]

    def is_ca_config_file(self) -> bool:
        return "CertificationAuthority" in self.filename

    def get_profile(self) -> ProfileType:
        if ProfileType.MTLSDEV.value in self.filename:
            return ProfileType.MTLSDEV
        return ProfileType.MTLS

    def get_config_parameter(self, parameter: str, mandatory: bool = False) -> str:
        """Returns the value to a parameter. If the value is empty or not present,
        an exception is thrown. It can be controlled via the parameter mandatory
        whether an empty value is ok or not."""
        for config_parameter in self.configParameter:
            if config_parameter[0] == parameter:
                if mandatory and not config_parameter[1]:
                    throw_configuration_exception(f"The configuration parameter {parameter} must not be empty!")
                return config_parameter[1]

    def __post_init__(self):
        self.rootDir = os.path.join(self.rootDir, self.get_profile().name)
        if not self.is_ca_config_file():
            app_name = self.get_config_parameter("server.ssl.applicationName")
            self.private_key_filename = f"{app_name}_private_key.pem"
            self.certificate_filename = f"{app_name}_cert.pem"
            self.certificate_signing_request_filename = f"csr-for-{app_name}"
            self.key_store_filename = self.__get_filename_with_extension("server.ssl.key-store")
            self.trust_store_filename = self.__get_filename_with_extension("server.ssl.trust-store")
        else:
            self.ca_key_filename = self.__get_filename_with_extension("server.ssl.ca-key.file")
            self.ca_cert_filename = self.__get_filename_with_extension("server.ssl.ca-Cert.file")

    def __get_filename_with_extension(self, config_param: str) -> str:
        config_value = self.get_config_parameter(config_param)
        if config_param.find("store") > -1:
            return config_value if any(x in config_value for x in [".pfx", ".p12"]) else f"{config_value}.p12"
        else:
            return config_value if any(x in config_value for x in [".pem", ".crt"]) else f"{config_value}.pem"


@dataclass
class Profile:
    type: ProfileType
    ca_config: CertificateConfigFile
    client_configs: List[CertificateConfigFile]


profiles: List[Profile] = list()


def read_config_files(target_path: str):
    config_files: List[CertificateConfigFile] = list()
    for rootdir, _, files in os.walk(target_path):
        for file in files:
            if file.endswith(".var"):
                print(file)
                config_files.append(
                    CertificateConfigFile(rootdir, file, __load_config_files(__check_file_path(rootdir, file))))

    profiles.append(create_profile(config_files, ProfileType.MTLS))
    profiles.append(create_profile(config_files, ProfileType.MTLSDEV))


def create_profile(config_files: List[CertificateConfigFile], profile_type: ProfileType) -> Profile:
    ca_config: CertificateConfigFile
    client_configs: List[CertificateConfigFile] = list()
    for config_file in config_files:
        if config_file.get_profile() == profile_type:
            if config_file.is_ca_config_file():
                ca_config = config_file
            else:
                client_configs.append(config_file)
    if not ca_config:
        raise ConfigurationException(
            "No configuration found for the certificate authority in profile {profile_type}!".format(
                profile_type=profile_type.value))
    return Profile(profile_type, ca_config, client_configs)


def __load_config_files(path) -> list:
    retval = list()
    with open(path, 'r', encoding='utf-8') as infile:
        for line in infile:
            retval.append(__split_config_param(line))
    infile.close()
    return retval


def __split_config_param(param: str) -> tuple:
    retval = param.replace("\n", "").split("=", 1)
    if retval[1].find("${") > -1:
        print("export {var}=geheim; ".format(var=retval[1][retval[1].find("${") + 2:retval[1].find("}")]))
        # run_cli_command("export {var}=geheim; ".format(var=retval[1][retval[1].find("${") + 2:retval[1].find("}")]))
    return tuple(retval)


def __check_file_path(path: str, filename: str) -> str:
    """Checks if file path and file exist and returns the file path in the format of the operating system
    otherwise throws on throw_configuration_exception"""
    filepath = os.path.join(path, filename)
    if not os.path.exists(filepath):
        throw_configuration_exception("Directory or file not found! {file}".format(file=filepath))
    return filepath


def throw_configuration_exception(message: str):
    logging.error(message)
    raise ConfigurationException(message)


# -----------------------------------------------------------------------------------------
# OpenSSL functions
def check_openssl_is_installed():
    __is_installed("openssl")


def check_keytool_is_installed():
    __is_installed("keytool")


def __is_installed(command: str):
    if which(command) is None:
        throw_configuration_exception(
            f"The command '{command}' was not found! This command is required to create the certificates!")


def __check_password(password: str) -> str:
    password = check_empty_sys_env_var(password)
    if password == "":
        throw_configuration_exception("The system variable is not set or the password is empty!")
    return password


def create_certificate_authority(config_file: CertificateConfigFile):
    """Generate a certificate authority by given CertificateConfigFile"""
    absolute_path = check_empty_sys_env_var(config_file.rootDir)
    ca_key_filename = os.path.join(absolute_path, config_file.ca_key_filename)
    ca_cert_filename = os.path.join(absolute_path, config_file.ca_cert_filename)
    ca_password = check_empty_sys_env_var(
        config_file.get_config_parameter("server.ssl.server.ca-password", mandatory=True))
    cipher = check_empty_sys_env_var(config_file.get_config_parameter("server.ssl.cipher", mandatory=True))
    bit_length = check_empty_sys_env_var(config_file.get_config_parameter("server.ssl.bitLength", mandatory=True))
    subject = check_empty_sys_env_var(config_file.get_config_parameter("server.ssl.subject", mandatory=True))
    cert_type = check_empty_sys_env_var(
        config_file.get_config_parameter("server.ssl.certificateStandard", mandatory=True))
    expiration_time_days = check_empty_sys_env_var(
        config_file.get_config_parameter("server.ssl.key-store.validityInDays"))

    # Create profile directory
    run_cli_command(f"mkdir -p {absolute_path}")

    # Generating a private key for the certificate authority
    run_cli_command(
        f"openssl genrsa -{cipher} -passout pass:\"{ca_password}\" -out \"{ca_key_filename}\" {bit_length}")
    # Generating a certificat for the certificate authority
    run_cli_command(
        f"openssl req -new -passin pass:\"{ca_password}\" -key \"{ca_key_filename}\" -{cert_type} "
        f"-days {expiration_time_days} -out {ca_cert_filename} -subj \"/CN={subject}\"")

    # run commands for generating ca key file and cert file
    # generate_rsa_privat_key(path, private_key_filename, password, cipher, bitrate)
    # generate_public_key(path, filename, password, subject, cert_type, expiration_time_days)


def create_client_keystore(profile: Profile):
    """Generate for each client a java keystore by given spring boot config profile"""
    create_certificate_authority(profile.ca_config)

    ca_absolute_path = check_empty_sys_env_var(profile.ca_config.rootDir)
    cipher = check_empty_sys_env_var(profile.ca_config.get_config_parameter("server.ssl.cipher"))
    ca_key_file = os.path.join(ca_absolute_path, profile.ca_config.ca_key_filename)
    ca_cert_file = os.path.join(ca_absolute_path, profile.ca_config.ca_cert_filename)
    ca_name = profile.ca_config.get_config_parameter("server.ssl.ca-name", mandatory=True)
    ca_password = profile.ca_config.get_config_parameter("server.ssl.server.ca-password", mandatory=True)

    for client_config in profile.client_configs:
        absolute_path = check_empty_sys_env_var(client_config.rootDir)
        csr_file = os.path.join(absolute_path, client_config.certificate_signing_request_filename)
        client_cert_file = os.path.join(absolute_path, client_config.certificate_filename)
        client_key_file = os.path.join(absolute_path, client_config.private_key_filename)
        client_keystore_file = os.path.join(absolute_path, client_config.key_store_filename)
        client_truststore_file = os.path.join(absolute_path, client_config.trust_store_filename)
        client_name = client_config.get_config_parameter("server.ssl.applicationName", mandatory=True)

        bit_length = check_empty_sys_env_var(client_config.get_config_parameter("server.ssl.bitLength"))
        expiration_time_days = check_empty_sys_env_var(
            client_config.get_config_parameter("server.ssl.key-store.validityInDays", mandatory=True))
        key_store_password = check_empty_sys_env_var(
            client_config.get_config_parameter("server.ssl.key-store-password", mandatory=True))
        trust_store_password = check_empty_sys_env_var(
            client_config.get_config_parameter("server.ssl.trust-store-password", mandatory=True))
        subject = check_empty_sys_env_var(client_config.get_config_parameter("server.ssl.subject", mandatory=True))

        # Create profile directory
        run_cli_command(f"mkdir -p {absolute_path}")

        # Generating a private key for the client
        run_cli_command(
            f"openssl genrsa -{cipher} -passout pass:\"{key_store_password}\" -out \"{client_key_file}\" {bit_length}")

        # Generating a certificate-signing request for the client
        run_cli_command(f"openssl req -passin pass:{key_store_password} -new -key {client_key_file} "
                        f"-out {csr_file} -subj \"/CN={subject}\"")

        # # Generating the client's CA signed certificate
        run_cli_command(
            f"openssl x509 -req -passin pass:{ca_password} -days {expiration_time_days} -in {csr_file} "
            f"-CA {ca_cert_file} -CAkey {ca_key_file} -set_serial 01 -out {client_cert_file}")

        # Creating a Java KeyStore with the clients’s public/private keys
        run_cli_command(
            f"openssl pkcs12 -export -passout pass:{key_store_password} -passin pass:{key_store_password} "
            f"-in {client_cert_file} -inkey {client_key_file} -out {client_keystore_file} -name {client_name}")

        # Remove truststore and creating a new Java TrustStore with the ca’s certificate
        run_cli_command(f"rm -rf {client_truststore_file}")
        run_cli_command(
            f"keytool -import -file {ca_cert_file} -alias {ca_name} -noprompt -keystore {client_truststore_file} "
            f"-storepass {trust_store_password}")


def run_cli_command(command: str):
    print("command: " + command)
    if command == "" or command is None:
        throw_configuration_exception("Command can't be empty!")
    proc = subprocess.run(command, shell=True)
    if proc.returncode != 0:
        throw_configuration_exception(f"Command has an error: {command}")


# -----------------------------------------------------------------------------------------


def generate_cetificates():
    for profile in profiles:
        create_client_keystore(profile)


def main():
    print(target_path)
    logging.info('Started')
    check_openssl_is_installed()
    check_keytool_is_installed()
    read_config_files(target_path)
    generate_cetificates()
    logging.info('Finished')


if __name__ == '__main__':
    logging.basicConfig(filename=os.path.join(target_path, "generateCertificates.log"), level=logging.INFO,
                        format='%(asctime)s %(levelname)s %(message)s')

    try:
        main()
    except SystemExit as err:
        pass
    except ConfigurationException as err:  # catch *all* exceptions
        print("Es ist ein Fehler aufgetreten: {0}\n{1}\n{2}".format(__file__, err, traceback.format_exc()))
        sys.exit(1)
