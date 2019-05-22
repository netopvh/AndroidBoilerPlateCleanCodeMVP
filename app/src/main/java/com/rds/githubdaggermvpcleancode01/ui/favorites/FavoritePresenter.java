package com.rds.githubdaggermvpcleancode01.ui.favorites;

import com.rds.githubdaggermvpcleancode01.data.DataManager;
import com.rds.githubdaggermvpcleancode01.data.network.NetworkError;
import com.rds.githubdaggermvpcleancode01.ui.base.BasePresenter;

import java.io.Serializable;

import io.reactivex.disposables.Disposable;

public class FavoritePresenter extends BasePresenter<FavoritesView, Serializable> implements FavoritesPresenterContract {
    private final DataManager dataManager;

    public FavoritePresenter(DataManager mDataManager) {
        super(mDataManager);
        this.dataManager = mDataManager;
    }

    @Override
    public void onRequestSuccess(Serializable favUserList) {
        super.onRequestSuccess(favUserList);
        mView.handleResult(favUserList);
        mView.hideLoading();
    }

    @Override
    public void onRequestError(NetworkError networkError) {
        super.onRequestError(networkError);
        mView.onFailure(networkError.getAppErrorMessage());
        mView.hideLoading();
    }

    @Override
    public void getFavoriteList() {
        Disposable disposable = dataManager.getAllFavoriteUsers(this);
        mDisposables.add(disposable);
    }

    @Override
    public void setView(FavoritesView view) {
        this.mView = view;
    }
}
