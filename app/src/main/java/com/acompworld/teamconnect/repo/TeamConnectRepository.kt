package com.acompworld.teamconnect.repo

import com.acompworld.teamconnect.api.model.requests.IncidenceEditRequest
import com.acompworld.teamconnect.api.model.requests.IncidenceReportRequest
import com.acompworld.teamconnect.api.model.requests.LoginRequest
import com.acompworld.teamconnect.api.service.TeamConnectApi
import com.acompworld.teamconnect.ui.fragments.home.Section

class TeamConnectRepository(val api : TeamConnectApi){

    suspend fun login(loginRequest: LoginRequest) = api.login(loginRequest)

    suspend fun getMyWall(projectCode: String) = api.getMyWall(projectCode)

    suspend fun getDirectory(projectCode: String, search: String) =
        api.getDirectory(projectCode, search)

    suspend fun  getEmployeeByID(projectCode: String, empID: String)=
        api.getEmployeeById(projectCode,empID)

    suspend fun getContactByID(projectCode: String, empID: String)=
        api.getContactById(projectCode,empID)

   suspend fun  getAboutUs(projectCode: String) = api.getAboutUs(projectCode)

    suspend fun getDepartments(projectCode: String, search: String) =
    api.getDepartments(projectCode,search)

    suspend fun getDepartmentInfo(projectCode: String, deptId : Int) =
        api.getDepartmentInfo(projectCode,deptId)

    suspend fun getDocs(projectCode: String, deptId : Int, rId : Int) =
     api.getDocs(projectCode,deptId,rId)

    suspend fun getGallery(projectCode: String, year : Int)=
        api.getAlbums(projectCode,year)

    suspend fun getPhotos(projectCode: String,albumID : Int)=
        api.getPhotos(projectCode,albumID)

    suspend fun getInfoList(projectCode: String, type: Section)=
        api.getInfoList(projectCode,type)

    suspend fun getInfo(projectCode: String,section: Section, rId: Int)=
        api.getInfo(projectCode,section,rId)

    suspend fun search(projectCode: String, search: String?)=
        api.search(projectCode,search)

    suspend fun getLMS(projectCode: String,userid:Int)=
        api.getLMS(projectCode, userid)

    suspend fun getFilteredIncidence(projectCode: String, filter: String, userid: Int) =
        api.getFilteredIncidence(projectCode, filter, userid)

    suspend fun getAllIncidence(projectCode: String, filter: String)=
        api.getAllIncidence(projectCode,filter)

    suspend fun getIncidenceDetail(projectCode: String, incidenceId:Int)=
        api.getIncidenceDetail(projectCode,incidenceId)

    suspend fun editIncidence(incidenceEditRequest: IncidenceEditRequest)=
        api.editIncidence(incidenceEditRequest)

    suspend fun reportIncidence(incidenceReportRequest: IncidenceReportRequest)=
        api.reportIncidence(incidenceReportRequest)

    suspend fun getDownload(projectCode: String,search: String?) =
        api.getDownload(projectCode,search)
}