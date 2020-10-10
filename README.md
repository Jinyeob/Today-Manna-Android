# Today_manna
> 오늘의 묵상 범위를 community.jbch.org에 로그인 후 파싱하여 보여주는 앱입니다.
플레이스토어 다운로드: https://play.google.com/store/apps/details?id=com.manna.parsing2

## 기능
3.4
* 절 별 개행이 되지 않는 오류 해결

3.3.3
* 일요일에 비정상종료되는 오류 해결 (조건문 추가, 인텐트 안넘어가게 함)

3.3.2
* 공유버튼 추가

3.3.1
* 로그인창 툴바 삭제
* 로그인창 키보드가 텍스트 가리는 문제 해결
* 로그인버튼 디자인 변경
* 배경색 안뜨는 문제 해결

3.3.0
* 디자인 대폭 수정 (최윤서s 디자인)
* 아이콘 
* 메뉴로 화면 전환 -> bottom navigation view 로 변경 (만나, 맥체인)
* 로그인 페이지 스크롤 가능
* 새로고침 삭제, 앱정보(아이콘) -> 메뉴로 이동

3.2.0
* 속도향상을 위한 코드 리팩토링
* 텍스트 왼쪽 정렬
* '오늘의 맥체인' -> 메뉴로 이동 (추가 기능 추가를 위해)
* 텍스트 복사 가능

3.1.3
* 몇몇 구형폰에서 생기는 치명적 오류 해결 (AsyncTask 사용, 멀티스레드로 변경)
* 로그인 실패 시 앱이 비정상 종료되는 오류 해결 (nullException -> 말씀 구절 가져올때 null이 반환돼서 거기서 select하니까 문제가 있었음. null 조건문을 추가.)
* 화면회전 시 새로고침되는 현상 해결
* 화면 스크롤 가능하게 변경 (ScrollView)

3.0.0
* 만나 범위 뿐만 아니라, 해당 구절도 제공 (크롬 개발자도구 - network - XHR 필터. process 파일 파싱. 아래서 따로 서술.)
* 오늘의 맥체인 읽기 범위 제공 (http://www.bible4u.pe.kr/zbxe/read 에서 파싱)
* 기존 텍스트로 알려주던 내용을 프로세스 다이얼로그로 대체
* 정보 창 내용 수정
* 새로고침 플로팅아이콘 삭제 (메뉴로 통합)

1.0 ~ 3.0.0
* 로그인
* 로그인 자동저장
* 파싱 후 날짜, 만나 범위 출력
* 로그인 실패 exception 추가 (토스트)
* 노액션바, 툴바로 바꿈 (색상: 흰색)
* 버튼 기능 -> 툴바 메뉴로 옮김
* 디자인개선
* 새로고침 시, 날짜가 중복돼서 출력되는 현상 해결



## 기능 설명

### 로그인
1. 먼저, FirstAuthActivity에서 자동저장 여부를 확인합니다. <br>
2. 자동저장이 되어있다면, MainActivity로 로그인 정보와 함께 화면을 전환합니다. <br>
2-1. 자동저장이 되어있지 않다면, LoginActivity로 화면 전환 후, 아이디와 패스워드를 입력합니다.
3. 로그인 정보를 가지고 사이트에 접속합니다.
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

#### 로그인 실패 처리
* 파싱한 결과 값(doc)이 null을 반환시,  토스트 및 로그인 화면으로 전환
* 쓰레드 내부에 토스트 출력 시 java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare() 뜸 -> 핸들러로 해결.
```java
if (doc == null) {
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "로그인 실패, 다시 로그인 해주세요.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            SaveSharedPreference.clearUser(MainActivity.this);
                            startActivity(intent);
                            finish();
                        }
                    }, 0);
                } 

```
### 세부 페이지 파싱
<p>
network 탭의 XHR 필터를 사용해서 묵상 구절을 올리는 페이지로 들어가보았다. 그리고 process.php 파일 프리뷰를 보아하니 아 이걸로 파싱하면 되겠다 싶었음. <br>
<img src="https://user-images.githubusercontent.com/37360089/72217613-cfadb380-3573-11ea-9e8b-e52b7b55a27d.png" width="60%"></img>
</p>
<p>
다른 같은 이름의 process.php 와 header값 차이점은 mode가 다르다는 것임!! load_post로 헤더값을 추가. post_uid는 파싱한 해당 url에서 substring으로 잘라서 가져왔음.<br>
<img src="https://user-images.githubusercontent.com/37360089/72217615-d0dee080-3573-11ea-8631-3a34b255dac1.png" width="60%"></img>
</p>

* div에서 가져온 더러운(?) url에서, onclick 속성의 값을 attr로 추출 후 replace를 사용하여 온전한 url 값으로 저장
```java
 getURL = doc.select("div.conbox.active div.content").first();
 
thumbURL = getURL
          .attr("onclick")
          .replace("getUrl('", "")
          .replace("', '')", "");
```

* post_uid 추출 (http://x/x/x/x.php?uid=3899409&x&x&x&x&x&x)
```java
String target = "?uid=";
int target_num = thumbURL.indexOf(target);
String result;
result = thumbURL.substring(target_num + 5, thumbURL.indexOf("&") + 1);
```

* 접속 (여기서는 get이 아니라 post로 가져온다. post 값을 추가하니깐..)
```java
//실제 말씀 구절 url로 접속
Document doc_bible = Jsoup.connect("http://community.jbch.org/meditation/board/process.php")
                    .header("Origin", "http://community.jbch.org")
                    .header("Referer", "http://community.jbch.org/")
                    .data("mode", "load_post")
                    .data("post_uid", result)
                    .cookies(loginCookie)
                    .post();
```

## 스크린샷

| 로그인 | 메인 화면 |
|:--------:|:--------:|
| <img src="https://user-images.githubusercontent.com/37360089/73274473-f4b74d00-4228-11ea-8e46-e6c63f1dc522.jpg" width="70%"></img> | <img src="https://user-images.githubusercontent.com/37360089/73274471-f41eb680-4228-11ea-9380-6cbdb77fc292.jpg" width="70%"></img> |

| 앱 정보 | 맥체인 |
|:--------:|:--------:|
| <img src="https://user-images.githubusercontent.com/37360089/73274470-f41eb680-4228-11ea-9d10-38aec97cb23d.jpg" width="70%"></img> | <img src="https://user-images.githubusercontent.com/37360089/73274472-f41eb680-4228-11ea-83a1-f73cad45cb16.jpg" width="70%"></img> |

## 도움받은 사이트
* 파싱: https://partnerjun.tistory.com/43?category=693285/ , https://partnerjun.tistory.com/42
* 세부 파싱(XHR): https://partnerjun.tistory.com/51
* 로그인 자동저장: https://bestcoding.tistory.com/7/
* bottom navigation view 관련: https://developer.android.com/reference/android/support/design/widget/BottomNavigationView, https://dev-imaec.tistory.com/11, 
* bottom navigation view 색상: https://stackoverflow.com/questions/40325422/selected-tabs-color-in-bottom-navigation-view
* frame layout 라운드처리: https://stackoverflow.com/questions/36208320/framelayout-with-rounded-corners
* 에디트텍스트 아이콘 사이즈: https://ddolcat.tistory.com/86
* 커스텀 폰트: https://lktprogrammer.tistory.com/191
* 키패드 나올때 자동 스크롤: https://tiann.tistory.com/13
* 클립보드 복사: https://yonoo88.tistory.com/1059
