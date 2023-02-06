package vishwakarma.matrimony.seva.util;

import android.database.Observable;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.Part;
import vishwakarma.matrimony.seva.model.BhartiModel;
import vishwakarma.matrimony.seva.model.ChatModel;
import vishwakarma.matrimony.seva.model.ContentModel;
import vishwakarma.matrimony.seva.model.FeesModel;
import vishwakarma.matrimony.seva.model.FileModel;
import vishwakarma.matrimony.seva.model.SignInModel;
import vishwakarma.matrimony.seva.model.SubCourseModel;
import vishwakarma.matrimony.seva.model.SwarankurModel;
import vishwakarma.matrimony.seva.model.UserModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {


    @POST(Constants.COURSE_URL)
    Call<List<BhartiModel>> getCourseList(@Body JSONObject jsonObject);
    @POST(Constants.SUB_COURSE_URL)
    Call<List<SubCourseModel>> getSubCourseList(@Query("mobile") String mobile, @Query("id")String id);

     @POST(Constants.CONTENT_LIST)
    Call<List<ContentModel>> getContentList(@Query("mobile") String mobile, @Query("id")String id);

    @POST(Constants.GET_USER_PROFILE)
    Call<List<UserModel>> getUserDetails(@Query("mobile") String mobile, @Query("id")String id, @Query("deviceid")String deviceid);

    @POST(Constants.GET_MATCH_PROFILE)
    Call<List<UserModel>> getMatches(@Query("mobile") String mobile, @Query("id")String id, @Query("deviceid")String deviceid);

    @POST(Constants.GET_MATCH_PROFILE_BY_TEXT)
    Call<List<UserModel>> getMatchesByStr(@Query("mobile") String mobile, @Query("id")String id, @Query("deviceid")String deviceid, @Query("str")String str);


    @POST(Constants.CHECK_LOGIN)
    Call<List<SignInModel>> checkLogin(@Query("mobile") String mobile, @Query("pass")String pass, @Query("deviceid")String deviceid);

    @POST(Constants.GET_USER_FEES)
    Call<List<FeesModel>> getFeesDetails(@Query("mobile") String mobile, @Query("id")String id);

    @POST(Constants.GET_USER_FILES)
    Call<List<FileModel>> getFileDetails(@Query("mobile") String mobile, @Query("id")String id);

    @POST(Constants.INSERTUSER)
    Call<String> addUser(@Query("name") String name,@Query("email")  String email,@Query("mobile")  String mobile,@Query("course")  String course,@Query("password")  String password,@Query("confirmpassword")  String confirmpassword,@Query("address")  String address);

    @POST(Constants.VALIDATEFEES)
    Call<String> validateFees(@Query("sid") String uid);

    @POST(Constants.GETSWARANKUR)
    Call<List<SwarankurModel>> getSwarankur(@Query("mobile")String mobile,@Query("id") String userid);

    @POST(Constants.INSERT_REGISTRATION)
    Call<String> insertUser(@Query("id") String id, @Query("usercategory") String usercategory, @Query("fullname") String fullname, @Query("dob") String dob, @Query("age") String age, @Query("email") String email,@Query("mobile1") String mobile1,@Query("mobile2")  String mobile2,@Query("isJanmpatrikaAvailable")  String isJanmpatrikaAvailable,@Query("height")  String height,@Query("color")  String color,@Query("gotra")  String gotra,@Query("isChashma")  String isChashma,@Query("qualification")  String qualification,@Query("jobdetails")  String jobdetails,@Query("joblocation")  String joblocation,@Query("monthlyIncome")  String monthlyIncome,@Query("local_address")  String local_address,@Query("permanent_Address")  String permanent_Address,@Query("isWishToSeeJamnpatrika")  String isWishToSeeJamnpatrika,@Query("isMangal")  String isMangal,@Query("hobbies")  String hobbies,@Query("partnerRequirment")  String partnerRequirment,@Query("fathername")  String fathername,@Query("fatherocupation")  String fatherocupation,@Query("mothersname")  String mothersname,@Query("totalbrothers")  String totalbrothers,@Query("bothersmaritialStatas")  String bothersmaritialStatas,@Query("totalsister")  String totalsister,@Query("sistermaritialStatus")  String sistermaritialStatus,@Query("MamacheKul")  String mamacheKul,@Query("photopath")  String photopath,@Query("createdDate")  String createdDate, @Query("password") String password,@Query("maritialstatus") String maritialstatus,@Query("birthtime") String birthtime,@Query("imageString") String imageString);

    @POST(Constants.UPDATE_REGISTRATION)
    Call<String> updateUser(@Query("id") String id, @Query("usercategory") String usercategory, @Query("fullname") String fullname, @Query("dob") String dob, @Query("age") String age, @Query("email") String email,@Query("mobile1") String mobile1,@Query("mobile2")  String mobile2,@Query("isJanmpatrikaAvailable")  String isJanmpatrikaAvailable,@Query("height")  String height,@Query("color")  String color,@Query("gotra")  String gotra,@Query("isChashma")  String isChashma,@Query("qualification")  String qualification,@Query("jobdetails")  String jobdetails,@Query("joblocation")  String joblocation,@Query("monthlyIncome")  String monthlyIncome,@Query("local_address")  String local_address,@Query("permanent_Address")  String permanent_Address,@Query("isWishToSeeJamnpatrika")  String isWishToSeeJamnpatrika,@Query("isMangal")  String isMangal,@Query("hobbies")  String hobbies,@Query("partnerRequirment")  String partnerRequirment,@Query("fathername")  String fathername,@Query("fatherocupation")  String fatherocupation,@Query("mothersname")  String mothersname,@Query("totalbrothers")  String totalbrothers,@Query("bothersmaritialStatas")  String bothersmaritialStatas,@Query("totalsister")  String totalsister,@Query("sistermaritialStatus")  String sistermaritialStatus,@Query("MamacheKul")  String mamacheKul,@Query("photopath")  String photopath,@Query("createdDate")  String createdDate, @Query("password") String password);

    @POST(Constants.INSERT_INTEREST)
    Call<String> addInterest(@Query("userid") String userid, @Query("interestedid") String interestid);

    @POST(Constants.INSERT_INTEREST_ACCEPT)
    Call<String> addInterest_Accept(@Query("userid") String userid, @Query("interestedid") String interestid, @Query("type") int type);

    @POST(Constants.INSERT_CHAT)
    Call<String> addChat(@Query("userid") String userid, @Query("interestedid") String interestid, @Query("message") String message);

    @POST(Constants.GET_CHAT)
    Call<List<ChatModel>> getChat(@Query("sender") String sender,@Query("receiver")  String receiver);

    @POST(Constants.GET_MATECHES_REQUEST)
    Call<List<UserModel>> getMatchesRequest(@Query("mobile") String mobile,@Query("userid")  String userid,@Query("deviceid")  String deviceid,@Query("type")  String type);

    @POST(Constants.GET_USERACTIVATION)
    Call<List<SignInModel>> checkActivation(@Query("mobile") String mobile,@Query("id")  String id);


    @POST(Constants.GET_USER_PROFILE_EMAIL)
    Call<List<UserModel>> getUserDetails_ForgetPassword(@Query("data")String data);

    @POST(Constants.EMAILSEND)
    Call<String> sendMail(@Body JsonObject loginResponse);



    @Multipart
    @POST("attachments/upload")
    Observable<ResponseBody> updateProfile(@Part("user_id") RequestBody id,
                                           @Part("full_name") RequestBody fullName,
                                           @Part MultipartBody.Part image,
                                           @Part("other") RequestBody other);

    @Multipart
    @POST("upload.php")
    Call<String> uploadImage(@Part("file") RequestBody file, @Part("desc") RequestBody desc);

    @Multipart
    @POST("upload.php?documentType=8")
    Call<String> uploadProductQualityImage(@Part MultipartBody.Part file,@Part("files") RequestBody items);

/*
    @POST(police.bharti.katta.util.Constants.BHARTI_URL_INDIVISUAL)
    Call<List<BhartiModel>> getBhartiMenuDetails(@Query("mobile") String mobile,@Query("id")String id,@Query("type")String type);

    @POST(police.bharti.katta.util.Constants.SARAV_MENU_URL)
    Call<List<SaravMenuModel>> getSaravMenu(@Body JSONObject jsonObject);

    @POST(police.bharti.katta.util.Constants.CHALUGHADAMODI_MENU_URL)
    Call<List<ChaluGhadamodiModel>> getChalughadaModiMenu(@Body JSONObject jsonObject);

    @POST(police.bharti.katta.util.Constants.GET_BOOKS_LIST)
    Call<List<BookModel>> getBookList(@Body JSONObject jsonObject);

    */

}
