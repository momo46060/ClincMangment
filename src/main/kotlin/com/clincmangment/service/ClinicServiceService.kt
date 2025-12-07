package com.clincmangment.service

import com.clincmangment.model.ClinicService
import com.clincmangment.repository.ClinicServiceRepository
import org.springframework.stereotype.Service

@Service
class ClinicServiceService(
    val repo: ClinicServiceRepository,
    val doctorRepo: UserServiceImpl
) {
    fun deleteVisitService(visitServiceId: Long?) = repo.deleteById(visitServiceId!!)
    fun getClinicServiceById(id: Long) = repo.findById(id).get()
    fun addService(doctorId: Long, name: String, price: Double) {
        val doctor = doctorRepo.findById(doctorId).orElseThrow()

        val service = ClinicService(name = name, price = price, doctor = doctor)

        repo.save(service)
    }
    fun getDoctorServices(doctorId: Long) = repo.findByDoctorId(doctorId)

    fun deleteService(id: Long) = repo.deleteById(id)
}
