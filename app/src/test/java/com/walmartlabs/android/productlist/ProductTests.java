package com.walmartlabs.android.productlist;


import android.app.Application;
import android.util.Log;

import com.walmartlabs.android.productlist.api.ApiError;
import com.walmartlabs.android.productlist.api.ApiManager;
import com.walmartlabs.android.productlist.api.ApiUtils;
import com.walmartlabs.android.productlist.api.WalmartLabsApi;
import com.walmartlabs.android.productlist.data.models.Product;
import com.walmartlabs.android.productlist.data.models.ProductsResponse;
import java.net.HttpURLConnection;
import java.util.concurrent.CountDownLatch;
import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ProductTests {

    @Rule public TestName name = new TestName();

    private ApiError apiError;

    private static final String TAG = ProductTests.class.getSimpleName();

    public CountDownLatch latch;

    private ProductsResponse productsResponse;

    // functional test. This is my first test and I am starting to implement the network layer.
    // i am doing test driven development so this is my way of figuring out the endpoints etc.

    /***
     * call the real endpoint with 0/30 as the starting/num items.
     * this test should succeed
     */
    @Test
    public void testProductPage1_Page2() throws Exception {

        Log.d(TAG, "testProductDownloadSuccess()");

        TheApplication application = (TheApplication) RuntimeEnvironment.application;

        String baseUrl = "https://walmartlabs-test.appspot.com/_ah/api/walmart/v1/";

        try {
            latch = new CountDownLatch(1);
            ApiManager apiManager = application.getApiManager();

            assertNotNull(name + ", apiManager should not be null", apiManager);

            apiManager.getApi(new ApiUtils(baseUrl)).getProducts(0, 20, new WalmartLabsApi.ProductListener() {
                @Override
                public void onFeedSuccess(final ProductsResponse theProductsResponse) {

                    productsResponse = theProductsResponse;
                    latch.countDown();
                }

                @Override
                public void onFailure(final ApiError apiError) {
                    fail(name + ", onFailure() statusCode=" + apiError.getHttpStatusCode()
                        + ", message=" + apiError.getmessage());
                    latch.countDown();
                }
            });
            latch.await();
            assertNotNull(name + ", product list should not be null", productsResponse);
            assertTrue(name + ", product list size should be > 0" , productsResponse.getProducts().size() > 0);
            assertEquals(name + ", status should be 200" , Integer.valueOf(200),productsResponse.getHttpStatus());
            assertTrue(name + ", total products should be > 0 " , productsResponse.getTotalProducts() > 0);
            assertTrue(name + ", etag length > 0" , productsResponse.getEtag().length() > 0);
            assertEquals(name + ", etag length > 0" , Integer.valueOf(20),productsResponse.getPageSize());
            assertTrue(name + ", kind length > 0" , productsResponse.getKind().length() > 0);
            assertTrue(name + ", id length > 0" , productsResponse.getId().length() > 0);
            assertEquals(name + ", page number should be 0" ,Integer.valueOf(0), productsResponse.getPageNumber());

            // now test that we can go to page 2

            int nextPage = productsResponse.getPageNumber() + 1;

            productsResponse = null;
            latch = new CountDownLatch(1);

            apiManager.getApi(new ApiUtils(baseUrl)).getProducts(nextPage, 20, new WalmartLabsApi.ProductListener() {
                @Override
                public void onFeedSuccess(final ProductsResponse theProductsResponse) {

                    productsResponse = theProductsResponse;
                    latch.countDown();
                }

                @Override
                public void onFailure(final ApiError apiError) {
                    fail(name + ", onFailure() statusCode=" + apiError.getHttpStatusCode()
                        + ", message=" + apiError.getmessage());
                    latch.countDown();
                }
            });
            latch.await();

            assertEquals(name + ", page number should be 2" ,Integer.valueOf(1), productsResponse.getPageNumber());

            // this proves that we can move up pages


        }catch(Exception e) {
            fail(name  + ", failed fatally e=" + e.getMessage());
        }

    }



    @Test
    public void testProductApiError() throws Exception {

        Log.d(TAG, "testProductApiError()");

        TheApplication application = (TheApplication) RuntimeEnvironment.application;

        String baseUrl = "https://wrong_domain-test.appspot.com/_ah/api/walmart/v1/";
        try {
            latch = new CountDownLatch(1);
            ApiManager apiManager = application.getApiManager();

            assertNotNull(name + ", apiManager should not be null", apiManager);

            apiManager.getApi(new ApiUtils(baseUrl)).getProducts(0, 20, new WalmartLabsApi.ProductListener() {
                @Override
                public void onFeedSuccess(final ProductsResponse theProductsResponse) {
                    fail(name + " this should never happen, url is wrong");
                    latch.countDown();
                }

                @Override
                public void onFailure(final ApiError theApiError) {
                    apiError = theApiError;
                    latch.countDown();
                }
            });
            latch.await();

            assertTrue(name +  ", status code should be 404 not found" , apiError.getHttpStatusCode() == HttpURLConnection.HTTP_NOT_FOUND);

        }catch(Exception e) {
            fail(name  + ", failed fatally e=" + e.getMessage());
        }

    }




}
