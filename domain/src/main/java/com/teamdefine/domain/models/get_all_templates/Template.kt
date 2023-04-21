package com.teamdefine.domain.models.get_all_templates

data class Template(
    val template_id: String,
    val title: String,
    val updated_at: Long,
    val signer_roles: ArrayList<SignerRoles>
)
