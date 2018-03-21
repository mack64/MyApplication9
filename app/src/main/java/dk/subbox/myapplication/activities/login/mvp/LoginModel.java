package dk.subbox.myapplication.activities.login.mvp;

import android.app.Activity;
import android.os.Build;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import dk.subbox.myapplication.activities.Hometest.HomeTestActivity;
import dk.subbox.myapplication.app.network.AuthNetwork;
import dk.subbox.myapplication.ext.LoginUser;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.SignatureException;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import timber.log.Timber;


public class LoginModel {

    private final Activity activity;
    private final AuthNetwork authNetwork;

    public LoginModel(Activity activity, AuthNetwork authNetwork){
        this.activity = activity;
        this.authNetwork = authNetwork;
    }

    void startHomeActivity(){
        HomeTestActivity.start(activity);
        activity.finish();
    }

    String getDeviceName(){
        return Build.DEVICE;
    }

    Observable<ResponseBody> attemptLogin(LoginUser.Builder userBuilder){
        LoginUser user;
        user = userBuilder.build();
        Timber.i("++++++++++++++++++++++++ username: " + user.username());
        Timber.i("++++++++++++++++++++++++ password: " + user.password());
        Timber.i("++++++++++++++++++++++++ device_name: " + user.device_name());

        Observable<ResponseBody> reposne = authNetwork.LOGIN_USER_OBSERVABLE(user);
        return reposne;
    }

    Jwt VerifyJWTTest2(String token) throws IOException, CertificateException, SignatureException, PrematureJwtException{
        InputStream is = activity.getAssets().open("certs/domain1.crt");

        BufferedInputStream bis = new BufferedInputStream(is);

        //InputStream is = new ByteArrayInputStream("MMKDxLDCgsygAwIBAgIBIDANCgYJKseIx7cNCgEBCwUgMHsxDzANCgYDVQQNCgwGU3ViYm94MQ8wDQoGA1UEAwwGU3ViYm94MQswCQYDVQQGEwJESzEXMBUGA1UECAwOUmFuZGVyc0tvbW11bmUxEDAOBgNVBAcMB1JhbmRlcnMxHzAdBgkqx4jHtw0KAQkBFhBtYWdudXNAc3ViYm94LmRrMB4XDQoxODAzMjAyMTA5MDhaFw0KMTkwMzIwMjEwOTA4WjB7MQ8wDQoGA1UEDQoMBlN1YmJveDEPMA0KBgNVBAMMBlN1YmJveDELMAkGA1UEBhMCREsxFzAVBgNVBAgMDlJhbmRlcnNLb21tdW5lMRAwDgYDVQQHDAdSYW5kZXJzMR8wHQYJKseIx7cNCgEJARYQbWFnbnVzQHN1YmJveC5kazDCgSIwDQoGCSrHiMe3DQoBAQEFIAPCgQ8gMMKBDQoCwoEBINOc4paz77+977+9A/GwiIoSe1Dvv73vv70qZmXVo8WySCP0ga+5yK/KuX3Gku6ZjzAt77+977+9edyfQcWT85OuqQ7UsfGsnKnLtsy1zYpRx77iiI94OO2fiu+/vc6mzqjtn7fvv73PnzXVvnUG4KC6EVsj5oaBzKFWHnrvv73vv71K6oSUcMOcNMasybom0aU8Wt2kax4uWlENCt+axJETwodM84G9pT7um6/Evs2KDs2vTD4oD82zKe+/ve+/vWciRQVlX8+D8quQnMKY0oHxkIqOUFXQgcmjVlY474+p5rCizqlaEmkf77+977+9AdOPA8+5OQYH5r+2QemWm2Lfpt25aCgry6/pgazEojkCAwEgAcSTMFEwHQYDVR0OBBYEFB/coEADNxcmyLNVWcyKDMeIUx3vv73vv70DVR0jBBgwFhQf3KBAAzcXJsizVVnMigzHiFMd77+977+9A1UdEwEB77+977+9AwEB77+977+9CSrHiMe3DQoBAQsFIAPCgQEgZd2jKNelzJs1PnAgfl5A36beiQ8IaMWgRi/Rh9uM4KCUyZLUn+yfh2gzIcS83YwV8aeiguarqnUNCm8qb9ecO+Gknw0K7K2Uw7ko4Yi+1pB4f9KOdcWu36FS872PmNmUcMSYwq48VhZz2bEFz5btmJjOv++/ve+/vdGJ17vYn+WMlFDTs92n0bDDhSZkVcqUSXIiyYPIgua5sfKFubwlS8W30KJc1ozElcKUzroWBMyf04NJT3vFg/SOiYExW+a6u++/ve+/vdON77+9HMO9zJNDzo4LyZ3IhnPgoIvVjNK217pJKUzTrgjeiPCsrKUgNPKVgqMQZTsdy6AJ3ZUQw47lvow=".getBytes());

        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(bis);

        JwtParser jwtParser = Jwts.parser().setSigningKey(certificate.getPublicKey());
        Jwt jwt = jwtParser.parse(token);
        return jwt;
    }




}
