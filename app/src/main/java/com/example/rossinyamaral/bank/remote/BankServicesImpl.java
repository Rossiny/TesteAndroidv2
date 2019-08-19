package com.example.rossinyamaral.bank.remote;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rossinyamaral.bank.ApiCallback;
import com.example.rossinyamaral.bank.BuildConfig;
import com.example.rossinyamaral.bank.model.StatementModel;
import com.example.rossinyamaral.bank.model.UserAccountModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rossinyamaral on 08/12/18.
 */

public class BankServicesImpl implements BankServices {

    private static final String TAG = BankServicesImpl.class.getSimpleName();

    private static final String POSTMAN_TOKEN = BuildConfig.POSTMAN_TOKEN;

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;

    private static final String USER_PARAM = "user";
    private static final String PASSWORD_PARAM = "password";

    private Context context;
    private Gson gson;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private Uri uri = Uri.parse(BuildConfig.SERVICE_BASE_URL);

    public BankServicesImpl(Context context) {
        this.context = context;
        gson = new Gson();
    }

    private void doRequest(int requestMethod, String url, final Map<String, String> map,
                           final Response.Listener<JSONObject> listener,
                           final Response.ErrorListener errorListener) {
        Log.d(TAG, url + " " + (map != null ? map.toString() : ""));
        StringRequest request = new StringRequest(requestMethod, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            listener.onResponse(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        errorListener.onErrorResponse(error);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("cache-control", "no-cache");
                params.put("Postman-Token", POSTMAN_TOKEN);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams() {
                return map;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                3,
                0));

        addToRequestQueue(request);
    }


    @Override
    public void login(final String user, final String password,
                      final ApiCallback<UserAccountModel> callback) {

        try {
            Map<String, String> map = new HashMap<>();
            map.put(USER_PARAM, user);
            map.put(PASSWORD_PARAM, password);
            Uri uri = this.uri.buildUpon().appendPath("login").build();
            Response.Listener<JSONObject> okListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (callback != null) {
                            JSONObject jsonError = response.getJSONObject("error");
                            if (jsonError.length() != 0) {
                                ErrorResponse error = gson.fromJson(
                                        jsonError.toString(), ErrorResponse.class);
                                callback.onError(error);
                                return;
                            }

                            JSONObject jsonObject = response.getJSONObject("userAccount");
                            UserAccountModel userAccountModel = gson.fromJson(
                                    jsonObject.toString(), UserAccountModel.class);

                            callback.onSuccess(userAccountModel);
                        }
                    } catch (Exception e) {
                        ErrorResponse errorResponse = new ErrorResponse();
                        errorResponse.code = -1;
                        errorResponse.message = "Ops! An errorResponse has occurred";
                        callback.onError(errorResponse);
                    }
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (callback != null) {
                        ErrorResponse errorResponse = new ErrorResponse();
                        errorResponse.code = -1;
                        errorResponse.message = error.getMessage();
                        callback.onError(errorResponse);
                    }
                }
            };
            doRequest(Request.Method.POST, uri.toString(), map, okListener, errorListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getStatements(int userId, final ApiCallback<List<StatementModel>> callback) {

        try {
            Uri uri = this.uri.buildUpon()
                    .appendPath("statements")
                    .appendPath(String.valueOf(userId))
                    .build();
            Response.Listener<JSONObject> okListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (callback != null) {
                            JSONObject jsonError = response.getJSONObject("error");
                            if (jsonError.length() != 0) {
                                ErrorResponse error = gson.fromJson(
                                        jsonError.toString(), ErrorResponse.class);
                                callback.onError(error);
                                return;
                            }

                            JSONArray jsonArray = response.getJSONArray("statementList");
                            Type listType = new TypeToken<List<StatementModel>>(){}.getType();
                            List<StatementModel> statements = gson.fromJson(
                                    jsonArray.toString(), listType);

                            callback.onSuccess(statements);
                        }
                    } catch (Exception e) {
                        ErrorResponse errorResponse = new ErrorResponse();
                        errorResponse.code = -1;
                        errorResponse.message = "Ops! An errorResponse has occurred";
                        callback.onError(errorResponse);
                    }
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (callback != null) {
                        ErrorResponse errorResponse = new ErrorResponse();
                        errorResponse.code = -1;
                        errorResponse.message = error.getMessage();
                        callback.onError(errorResponse);
                    }
                }
            };
            doRequest(Request.Method.GET, uri.toString(), null, okListener, errorListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        return mRequestQueue;
    }

    private ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue, null
                    //new LruBitmapCache()
            );
        }
        return this.mImageLoader;
    }

    private <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    private <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
