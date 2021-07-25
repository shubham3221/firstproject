package com.example.s_tools;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;

import okhttp3.CertificatePinner;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;

public class NetworkHelper implements Interceptor {




//        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
//                .tlsVersions(TlsVersion.TLS_1_2)
//                .cipherSuites(
//                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
//                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
//                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
//                .build();
//
//        OkHttpClient client = new OkHttpClient.Builder()
//                .connectionSpecs(Collections.singletonList(spec))
//                .certificatePinner(new CertificatePinner.Builder()
//                        .add("drakeet.me", "sha256/gGOcYKAwzEaUfun6YdxZvFSQq/x2lF/R8UizDFofveY=")
//                        .build())
//                .build();


//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.level(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(interceptor)
//                .build();
//        return client;




    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        // Get reuqest info
        Request initialReq = chain.request();
        // Create modified request to return
        Request modRequest = initialReq;
        Response response = chain.proceed(modRequest);

        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(spec))
                .certificatePinner(new CertificatePinner.Builder()
                        .add("drakeet.me", "sha256/gGOcYKAwzEaUfun6YdxZvFSQq/x2lF/R8UizDFofveY=")
                        .build())
                .build();
        return response;
    }
    public static class InterLog{
        public static OkHttpClient getIntercept(){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            return client;
        }

    }
}
