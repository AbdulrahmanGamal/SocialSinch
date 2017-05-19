package com.social.tenor;

import com.social.tenor.data.DataManager;
import com.social.tenor.model.TenorModel;

import org.junit.Test;

import rx.observers.TestSubscriber;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TenorUnitTest {
    @Test
    public void searchByKeywordTest() throws Exception {
        TestSubscriber<TenorModel> testSubscriber = new TestSubscriber<>();
        DataManager.searchGiphyByKeyword("love", 1, "", "en_US").subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
//        List<GiphyModel> model = testSubscriber.getOnNextEvents();
//        Log.e("test", "model is: " + model);
    }
}