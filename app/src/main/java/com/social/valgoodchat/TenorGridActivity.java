package com.social.valgoodchat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.social.tenor.data.DataManager;
import com.social.tenor.model.TenorModel;
import com.social.valgoodchat.app.SocialSinchApplication;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

public class TenorGridActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    private String mGifNext;
    //private AnimatedGridViewAdapter mGifAdapter;
    private MenuItem mSearchItem;
    private Toolbar mToolbar;
    private String mKeywordSearch;
    private SearchView mSearchView;
    private CompositeSubscription mDisposable;
    private String mLocale;

    private final PublishSubject<String> mPaginator = PublishSubject.create();

    private static final int MAX_RESULTS = 10;

    private static final String TAG = "TenorGridActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_grid);

        GridView mGridView = (GridView) findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mKeywordSearch = "";
        mLocale = Locale.getDefault().toString();
//        mGifAdapter = new AnimatedGridViewAdapter(this, new ArrayList<>());
//        mGridView.setAdapter(mGifAdapter);
//        mGridView.setOnScrollListener(new EndlessScrollListener() {
//            @Override
//            public boolean onLoadMore(int page, int totalItemsCount) {
//                if (!"".equals(mGifNext)) {
//                    mPaginator.onNext(mGifNext);
//                    return true;
//                }
//
//                return false;
//            }
//
//            @Override
//            public void onScrollUp() {
//
//            }
//
//            @Override
//            public void onScrollDown() {
//
//            }
//        });

        mGifNext = "";
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.gif_galley_title));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.unsubscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDisposable.unsubscribe();
        SocialSinchApplication.activityPaused();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDisposable = new CompositeSubscription();
        attachPaginator();
        attachSearchObservable();
        mPaginator.onNext(mGifNext);
        SocialSinchApplication.activityResumed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        mSearchItem = menu.findItem(R.id.action_search);

        MenuItemCompat.setOnActionExpandListener(mSearchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Called when SearchView is collapsing
                if (mSearchItem.isActionViewExpanded()) {
                    animateSearchToolbar(1, false, false);
                    resetToTrending();
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Called when SearchView is expanding
                animateSearchToolbar(1, true, true);
                return true;
            }
        });
        mSearchView = (SearchView) mSearchItem.getActionView();
        mSearchView.findViewById(R.id.search_close_btn).setOnClickListener(view -> {
            resetToTrending();
            mSearchView.setQuery("", false);
        });
        attachSearchObservable();
        return true;
    }

    /**
     * Subscribes to the paginator observable
     */
    private void attachPaginator() {
        mDisposable.add(
            mPaginator
              .observeOn(Schedulers.io())
              .concatMap(next -> { if ("".equals(mKeywordSearch)) {
                                     return DataManager.INSTANCE.getTrending(MAX_RESULTS, next);
                                   }
                                   return DataManager.INSTANCE.searchGiphyByKeyword(mKeywordSearch, MAX_RESULTS, next, mLocale);
              })
              .filter(tenorModel -> tenorModel.getResults() != null)
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(tenorModel -> {
//                  mGifAdapter.setGridData(tenorModel.getResults());
                  mGifNext = tenorModel.getNext();
                  mProgressBar.setVisibility(View.INVISIBLE);
              })
        );
    }

    /**
     * Subscribes to the search text observable
     */
    private void attachSearchObservable() {
        if (mSearchView == null) {
            return;
        }
        mDisposable.add(observableTextChangeListenerWrapper(mSearchView)
                .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .filter(query -> query != null && !query.isEmpty() && query.length() > 2)
                .doOnNext(s -> mProgressBar.setVisibility(View.VISIBLE))
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<TenorModel>>() {
                    @Override
                    public Observable<TenorModel> call(String query) {
                        mKeywordSearch = query;
                        return DataManager.INSTANCE.searchGiphyByKeyword(query, MAX_RESULTS, "", mLocale);
                    }
                })
                .filter(tenorModel -> tenorModel.getResults() != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TenorModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error retrieving next gifs results " + e);
                    }

                    @Override
                    public void onNext(TenorModel tenorModel) {
//                        mGifAdapter.clearGridData();
//                        mGifAdapter.setGridData(tenorModel.getResults());
//                        mGifNext = tenorModel.getNext();
                        mProgressBar.setVisibility(View.INVISIBLE);

                    }
                })
        );
    }
    /**
     * Reset the adapter and show the current trending gifs
     */
    private void resetToTrending() {
        mKeywordSearch = "";
        mGifNext = "";
//        mGifAdapter.clearGridData();
        mPaginator.onNext(mGifNext);
    }
    /**
     * Wraps the ContextQuery into an observable to subscribe and get the search keywords
     * @param searchView
     * @return
     */
    public Observable<String> observableTextChangeListenerWrapper(final SearchView searchView) {
        return Observable.create(subscriber -> {
            SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // newText is text entered by user to SearchView
                    if (subscriber.isUnsubscribed()) {
                        searchView.setOnQueryTextListener(null);
                    } else {
                        subscriber.onNext(newText);
                    }
                    return false;
                }
            };
            searchView.setOnQueryTextListener(searchListener);
        });
    }

    /**
     * Performs Circular Reveal in the Search View
     * @param numberOfMenuIcon
     * @param containsOverflow
     * @param show
     */
    public void animateSearchToolbar(int numberOfMenuIcon, boolean containsOverflow, boolean show) {

        mToolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        //getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.ColorPrimary));

        if (show) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int width = mToolbar.getWidth() -
                        (containsOverflow ? getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) -
                        ((getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);
                Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(mToolbar,
                        isRtl(getResources()) ? mToolbar.getWidth() - width : width, mToolbar.getHeight() / 2, 0.0f, (float) width);
                createCircularReveal.setDuration(250);
                createCircularReveal.start();
            } else {
                TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (-mToolbar.getHeight()), 0.0f);
                translateAnimation.setDuration(220);
                mToolbar.clearAnimation();
                mToolbar.startAnimation(translateAnimation);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int width = mToolbar.getWidth() -
                        (containsOverflow ? getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material) : 0) -
                        ((getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) * numberOfMenuIcon) / 2);
                Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(mToolbar,
                        isRtl(getResources()) ? mToolbar.getWidth() - width : width, mToolbar.getHeight() / 2, (float) width, 0.0f);
                createCircularReveal.setDuration(250);
                createCircularReveal.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mToolbar.setBackgroundColor(ContextCompat.getColor(TenorGridActivity.this, R.color.ColorPrimary));
                        //getWindow().setStatusBarColor(ContextCompat.getColor(TenorGridActivity.this, R.color.ColorPrimary));
                    }
                });
                createCircularReveal.start();
            } else {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                Animation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (-mToolbar.getHeight()));
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(translateAnimation);
                animationSet.setDuration(220);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mToolbar.setBackgroundColor(ContextCompat.getColor(TenorGridActivity.this, R.color.ColorPrimary));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mToolbar.startAnimation(animationSet);
            }
            //getWindow().setStatusBarColor(ContextCompat.getColor(TenorGridActivity.this, R.color.ColorPrimary));
        }
    }

    private boolean isRtl(Resources resources) {
        return resources.getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }
}
