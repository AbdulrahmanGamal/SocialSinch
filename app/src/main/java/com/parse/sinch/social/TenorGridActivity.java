package com.parse.sinch.social;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.parse.sinch.social.adapter.TenorGridViewAdapter;
import com.parse.sinch.social.custom.EndlessScrollListener;
import com.social.tenor.data.DataManager;
import com.social.tenor.model.Result;
import com.social.tenor.model.TenorModel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class TenorGridActivity extends AppCompatActivity {
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private String mTrendingNext;
    private String mSearchNext;
    private TenorGridViewAdapter mTrendingAdapter;
    private TenorGridViewAdapter mSearchAdapter;
    private MenuItem mSearchItem;
    private Toolbar mToolbar;
    private String mKeywordSearch;

    private static final int MAX_RESULTS = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_grid);

        mGridView = (GridView) findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mTrendingNext = "";
        mSearchNext = "";
        mKeywordSearch = "";
        mTrendingAdapter = new TenorGridViewAdapter(this, new ArrayList<Result>());
        mSearchAdapter = new TenorGridViewAdapter(this, new ArrayList<Result>());
        mGridView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                boolean canSearchMore = "".equals(mKeywordSearch) && !"".equals(mTrendingNext) ||
                        !"".equals(mKeywordSearch) && !"".equals(mSearchNext);

                Log.e("TenorGridActivity", "mKeywordSearch: " + mKeywordSearch);
                Log.e("TenorGridActivity", "canSearchMore: " + canSearchMore);
                Log.e("TenorGridActivity", "mSearchNext: " + mSearchNext);
                return canSearchMore && makeGifRequest();
            }
        });
        //only called the first time the view is created
        makeGifRequest();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.gif_galley_title));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
        SearchView searchView = (SearchView) mSearchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // newText is text entered by user to SearchView
                //Toast.makeText(getApplicationContext(), newText, Toast.LENGTH_LONG).show();
//                mSearchAdapter.clearGridData();
//                mGridView.setAdapter(mSearchAdapter);
                if (newText != null && !newText.isEmpty() && newText.length() > 2) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mKeywordSearch = newText;
                    loadSearchDataFromApi(MAX_RESULTS);
                } else {
                    mSearchNext = "";
                    mKeywordSearch = "";
                    mSearchAdapter.clearGridData();
                }
                return false;
            }
        });
        return true;
    }

    private boolean makeGifRequest() {
        if (!"".equals(mKeywordSearch)) {
            loadSearchDataFromApi(MAX_RESULTS);
        } else {
            loadNextTrendingFromApi(MAX_RESULTS);
        }
        return true;
    }
    /**
     * Resets the adapter to trending gifs
     */
    private void resetToTrending() {
        loadNextTrendingFromApi(MAX_RESULTS);
        mTrendingNext = "";
        mKeywordSearch = "";
    }
    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextTrendingFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`
        DataManager.getTrending(offset, mTrendingNext)
                .filter(new Func1<TenorModel, Boolean>() {
                    @Override
                    public Boolean call(TenorModel tenorModel) {
                        return tenorModel.getResults() != null;
                    }
                })
                .take(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TenorModel>() {
                    @Override
                    public void call(TenorModel tenorModel) {
                        mTrendingAdapter.setGridData(tenorModel.getResults());
                        if ("".equals(mTrendingNext)) {
                            mGridView.setAdapter(mTrendingAdapter);
                        }
                        mTrendingNext = tenorModel.getNext();
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadSearchDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`
        String locale = Locale.getDefault().toString();
        DataManager.searchGiphyByKeyword(mKeywordSearch, offset, mSearchNext, locale)
                .delay(5, TimeUnit.MILLISECONDS)
                .filter(new Func1<TenorModel, Boolean>() {
                    @Override
                    public Boolean call(TenorModel tenorModel) {
                        return tenorModel.getResults() != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TenorModel>() {
                    @Override
                    public void call(TenorModel tenorModel) {
                        Log.e("TenorGridActivity", "adding search Results: " + mSearchNext);
                        mSearchAdapter.setGridData(tenorModel.getResults());
                        if ("".equals(mSearchNext)) {
                            mGridView.setAdapter(mSearchAdapter);
                        }
                        mSearchNext = tenorModel.getNext();
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }
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

//    private static int getThemeColor(Context context, int id) {
//        Resources.Theme theme = context.getTheme();
//        TypedArray a = theme.obtainStyledAttributes(new int[]{id});
//        int result = a.getColor(0, 0);
//        a.recycle();
//        return result;
//    }
}
