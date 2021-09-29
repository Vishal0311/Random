package com.acompworld.teamconnect.api.service

import com.acompworld.teamconnect.api.model.SearchResponse
import com.acompworld.teamconnect.api.model.requests.IncidenceEditRequest
import com.acompworld.teamconnect.api.model.requests.IncidenceReportRequest
import com.acompworld.teamconnect.api.model.requests.LoginRequest
import com.acompworld.teamconnect.api.model.responses.*
import com.acompworld.teamconnect.ui.fragments.home.Section
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TeamConnectApi {
    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @GET("mywall")
    suspend fun getMyWall(
        @Query("projcode") projcode: String
    ): Response<MyWallResponse>

    @GET("directory")
    suspend fun getDirectory(
        @Query("projcode") projectCode: String,
        @Query("search") search: String = ""
    ): Response<DirectoryResponse>


    @GET("empprofile")
    suspend fun getEmployeeById(
        @Query("projcode") projectCode: String,
        @Query("empcode") empCode: String
    ): Response<EmployeeResponse>


    @GET("contactinfo")
    suspend fun getContactById(
        @Query("projcode") projectCode: String,
        @Query("id") id: String
    ): Response<ContactResponse>

    @GET("about")
    suspend fun getAboutUs(
        @Query("projcode") projcode: String
    ): Response<AboutRespone>


    @GET("deptlist")
    suspend fun getDepartments(
        @Query("projcode") projcode: String,
        @Query("search") search: String = ""
    ): Response<DepartmentListResponse>

    @GET("deptinfo")
    suspend fun getDepartmentInfo(
        @Query("projcode") projcode: String,
        @Query("deptid") deptid: Int
    ): Response<DepartmentInfoResponse>

    @GET("deptdocs")
    suspend fun getDocs(
        @Query("projcode") projcode: String,
        @Query("deptid") deptid: Int,
        @Query("rid") rId: Int
    ): Response<DepartmentDocumentsResponse>

    @GET("albums")
    suspend fun getAlbums(
        @Query("projcode") projcode: String,
        @Query("year") year: Int
    ): Response<GalleryResponse>

    @GET("photos")
    suspend fun getPhotos(
        @Query("projcode") projcode: String,
        @Query("albumid") albumID: Int
    ): Response<PhotosReponse>

    @GET("infolist")
    suspend fun getInfoList(
        @Query("projcode") projcode: String,
        @Query("type") type: Section
    ): Response<InfoListResponse>

    @GET("info")
    suspend fun getInfo(
        @Query("projcode") projcode: String,
        @Query("type") type: Section,
        @Query("rid") rId: Int
    ): Response<InfoResponse>

    @GET("search")
    suspend fun search(
        @Query("projcode") projcode: String,
        @Query("search") search: String?
    ): Response<SearchResponse>

    @GET("lms/resources")
    suspend fun getLMS(
        @Query("projcode") projcode: String,
        @Query("userid") userid: Int
    ): Response<LMSResponse>

    @GET("incidence/list")
    suspend fun getFilteredIncidence(
        @Query("projcode") projcode: String,
        @Query("filter") filter: String,
        @Query("userid") userid: Int
    ): Response<IncidenceListResponse>

    @GET("incidence/list")
    suspend fun getAllIncidence(
        @Query("projcode") projcode: String,
        @Query("filter") filter: String
    ): Response<IncidenceListResponse>

    @GET("incidence/details")
    suspend fun getIncidenceDetail(
        @Query("projcode") projcode: String,
        @Query("incidenceid") incidenceid: Int
    ): Response<IncidenceDetailResponse>

    @POST("incidence/update")
    suspend fun editIncidence(
        @Body editRequest: IncidenceEditRequest
    ): Response<IncidenceEditResponse>

    @POST("incidence/add")
    suspend fun reportIncidence(
        @Body reportRequest: IncidenceReportRequest
    ): Response<IncidenceReportResponse>

    @GET("downloads")
    suspend fun getDownload(
        @Query("projcode") projcode: String,
        @Query("search") search: String?
    ): Response<DownloadResponse>
}