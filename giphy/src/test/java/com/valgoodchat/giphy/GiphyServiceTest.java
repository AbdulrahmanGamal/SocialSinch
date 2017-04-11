package com.valgoodchat.giphy;

import com.valgoodchat.giphy.data.DataManager;
import com.valgoodchat.giphy.model.GiphyModel;

import org.junit.Test;
import rx.observers.TestSubscriber;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GiphyServiceTest {
    @Test
    public void searchByKeywordTest() throws Exception {
        TestSubscriber<GiphyModel> testSubscriber = new TestSubscriber<>();
        DataManager.searchGiphyByKeyword("love").subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
//        List<GiphyModel> model = testSubscriber.getOnNextEvents();
//        Log.e("test", "model is: " + model);
    }
}