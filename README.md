# Today_manna
오늘의 묵상 범위를 community.jbch.org에 로그인 후 파싱하여 보여주는 앱입니다.

## 기능 (업데이트 중 - 1/8)
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
\
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
* 파싱한 결과 값(htmlContentInStringFormat, viewPageUrl)이 없을 때(\"\"),  토스트 및 로그인 화면으로 전환
* 쓰레드 내부에 토스트 출력 시 java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare() 뜸 -> 핸들러로 해결.
```java
 if(htmlContentInStringFormat.equals("")||viewPageUrl.equals("")){
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"로그인 실패, 다시 로그인 해주세요.",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            SaveSharedPreference.clearUser(MainActivity.this);
                            startActivity(intent);
                            finish();
                        }
                    }, 0);
                }

```

## 스크린샷

| <center>로그인</center> | <center>로그인 실패</center> |
|:--------:|:--------:|
| <img src="https://user-images.githubusercontent.com/37360089/71947640-e0c39100-3210-11ea-9238-e18f4f95d63c.jpg" width="70%"></img> | <img src="https://user-images.githubusercontent.com/37360089/71947650-e325eb00-3210-11ea-9024-3a11d262500e.jpg" width="70%"></img> |

| <center>메인 화면</center> | <center>메뉴</center> |
|:--------:|:--------:|
| <img src="https://user-images.githubusercontent.com/37360089/72217496-ef43dc80-3571-11ea-8ca2-225d83133e68.jpg" width="70%"></img>
 | <img src="https://user-images.githubusercontent.com/37360089/71947649-e325eb00-3210-11ea-9b85-7b04f9ed39b8.jpg" width="70%"></img> |

| 앱 정보 (i 아이콘) | 새로고침 |
|:--------:|:--------:|
| <img src="https://user-images.githubusercontent.com/37360089/71947642-e0c39100-3210-11ea-8eb5-69a9e36323ce.jpg" width="70%"></img> | <img src="https://user-images.githubusercontent.com/37360089/71948382-300ac100-3213-11ea-866f-2dcce579d2e9.jpg" width="70%"></img> |

## 출처
* 파싱: https://partnerjun.tistory.com/43?category=693285/
* 로그인 자동저장: https://bestcoding.tistory.com/7/

