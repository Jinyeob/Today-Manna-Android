# Today_manna
오늘의 묵상 범위를 community.jbch.org에 로그인 후 파싱하여 보여주는 앱입니다.

## 기능 (업데이트 중)
* 로그인
* 로그인 자동저장
* 파싱 후 날짜, 만나 범위 출력

## 기능 설명

### 로그인
1. 먼저, FirstAuthActivity에서 자동저장 여부를 확인합니다. <br>
2. 자동저장이 되어있다면, MainActivity로 로그인 정보와 함께 화면을 전환합니다. <br>
2-1. 자동저장이 되어있지 않다면, LoginActivity로 화면 전환 후, 아이디와 패스워드를 입력합니다.
3. '불러오기 버튼' 클릭 시, 로그인 정보를 가지고 사이트에 접속합니다.
```java
Connection.Response res = Jsoup.connect(htmlPageUrl)
                        .data("user_id", ID)
                        .data("saveid", "1")
                        .data("passwd", PASSWD)
                        .data("mode", "")
                        .data("go", "yes")
                        .data("url", "http://community.jbch.org/")
                        .data("LoginButton", "LoginButton")
                        .method(Connection.Method.POST)
                        .execute();
```
4. 접속 후 날짜 정보와 묵상 범위 이미지를 파싱하여 Elements 객체에 저장합니다.
5. 객체에 있는 내용을 TextView와 WebView에 출력합니다.


#### 로그인 자동저장
* SharedPreferences API를 사용
```java
public class SaveSharedPreference {
    private static final String PREF_USER_NAME = "username";
    private static final String PREF_USER_PASSWD = "userpasswd";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 계정 정보 저장
    public static void setUser(Context ctx, String userName, String userPasswd) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.putString(PREF_USER_PASSWD, userPasswd);

        editor.commit();
    }

    // 저장된 정보 가져오기
    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }
    public static String getUserPasswd(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_PASSWD, "");
    }
    // 로그아웃
    public static void clearUser(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}
```
## 스크린샷
<div>
  
#### 로그인
<center><img src="https://user-images.githubusercontent.com/37360089/71324459-20586080-2522-11ea-9b6d-da7cb8d17e86.jpg" width="40%"></img></center>

#### 메인 화면
<center><img src="https://user-images.githubusercontent.com/37360089/71324462-22222400-2522-11ea-88b1-b4d00edbdbe1.jpg" width="40%"></img></center>

#### 버튼 클릭 후
<center><img src="https://user-images.githubusercontent.com/37360089/71324463-23535100-2522-11ea-90a6-3987fdc8c3e2.jpg" width="40%"></img></center>

</div>

## 출처
* 파싱: https://partnerjun.tistory.com/43?category=693285/
* 로그인 자동저장: https://bestcoding.tistory.com/7/

