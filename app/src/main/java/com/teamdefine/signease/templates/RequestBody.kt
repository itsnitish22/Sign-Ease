package com.teamdefine.signease.templates

import android.util.Log
import com.teamdefine.signease.SignerDetails
import com.teamdefine.signease.api.models.get_all_templates.Template
import com.teamdefine.signease.api.models.post_template_for_sign.CustomFields
import com.teamdefine.signease.api.models.post_template_for_sign.Document
import com.teamdefine.signease.api.models.post_template_for_sign.Signers
import com.teamdefine.signease.api.models.post_template_for_sign.SigningOptions

class RequestBody {
    fun getRequestBody(reason:String,templateSelected: Template,currentUserDetail:MutableMap<String,Any>,dateSelectedByUser:String):Document{
        lateinit var tempSigners:Signers
        var customFields:ArrayList<CustomFields> = arrayListOf()
        var signers:ArrayList<Signers> = arrayListOf()
        val signerRole=templateSelected.signer_roles
        for(i in  signerRole){
            when(i.name){
                "HOD"-> {
                    signers.add(SignerDetails().hod)
                }
                "Warden"->{
                    signers.add(SignerDetails().warden)
                }
                "Parent"->{
                    signers.add(SignerDetails().parent)
                }
            }
        }
        val f1 = CustomFields("Student-Name", "${currentUserDetail.getValue("fullName")}")
        val f2 = CustomFields("Student-UID", "${currentUserDetail.getValue("uid")}")
        customFields.add(f1)
        customFields.add(f2)
        when(templateSelected.title){
            "Application For Duty Leave"->{
                val f3 = CustomFields("Leave-Start", dateSelectedByUser)
                customFields.add(f3)
            }
            "Day-Pass"->{
                val f5=CustomFields("Reason",reason)
                customFields.add(f5)
            }
            "Night-Pass"->{
                val f3 = CustomFields("Leave-Start", dateSelectedByUser)
                val f4 = CustomFields("Leave-End", dateSelectedByUser)
                val f5=CustomFields("Reason",reason)
                customFields.add(f3)
                customFields.add(f4)
                customFields.add(f5)
            }
        }
        val template_ids = arrayListOf(templateSelected.template_id)
        val subject = templateSelected.title
        val message = "Kindly review and approve my ${templateSelected.title}."
        val signingOptions = SigningOptions(true, true, true, false, "draw")
        Log.i("helloabc","HOD12")
        val document =
            Document(template_ids, subject, message, signers, customFields, signingOptions, true)
        return document
    }
}