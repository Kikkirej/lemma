package de.fhdo.lemma.visualizer.model

import de.fhdo.lemma.service.MicroserviceType

data class MicroserviceVertex(val name: String, val type: MicroserviceType)