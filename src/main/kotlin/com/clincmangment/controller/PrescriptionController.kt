package com.clincmangment.controller

import com.clincmangment.repository.UserRepository
import com.clincmangment.model.User
import com.clincmangment.service.PrescriptionService
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/doctor")
class PrescriptionController(
    private val prescriptionService: PrescriptionService,
    private val userRepository: UserRepository,
    private val httpSession: HttpSession
) {

    // حفظ الروشتة
    @PostMapping("/visit/save-prescription")
    fun savePrescription(
        @RequestParam visitId: Long,
        @RequestParam content: String
    ): String {
        prescriptionService.savePrescription(visitId, content)
        return "redirect:/doctor/visit/$visitId"
    }

    // جلب القوالب
    @GetMapping("/visit/templates")
    @ResponseBody
    fun getTemplates(): List<Map<String, Any>> {
        val currentUser = httpSession.getAttribute("loggedUser") as User
        return prescriptionService.getTemplatesByDoctor(currentUser).map {
            mapOf("id" to it.id, "name" to it.name, "content" to it.content) as Map<String, Any>
        }
    }

    // حفظ قالب
    @PostMapping("/visit/template/save")
    @ResponseBody
    fun saveTemplate(
        @RequestParam name: String,
        @RequestParam content: String
    ): Map<String, Any> {
        val currentUser = httpSession.getAttribute("loggedUser") as User
        val template = prescriptionService.saveTemplate(name, content, currentUser)
        return mapOf("id" to template.id, "name" to template.name) as Map<String, Any>
    }


}
