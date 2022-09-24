package com.teamdefine.signease.templates

import android.util.Log
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
                    Log.i("helloabc","HOD")
                    tempSigners = Signers("HOD", "Nitish", "nitish.sharma1186@gmail.com")
                    signers.add(tempSigners)
                }
                "Warden"->{                    Log.i("helloabc","warden")
                    tempSigners = Signers("Warden", "Nikhil", "nikhils.sharma2409@gmail.com")
                    signers.add(tempSigners)
                }
                "Parent"->{
                    tempSigners = Signers("Parent", "Aniket", "ani.khajanchi257@gmail.com")
                    signers.add(tempSigners)
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