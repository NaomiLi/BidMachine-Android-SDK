package io.bidmachine.ads.networks.mraid;

import androidx.annotation.NonNull;

import com.explorestack.iab.mraid.MraidError;
import com.explorestack.iab.mraid.MraidInterstitial;
import com.explorestack.iab.mraid.MraidInterstitialListener;
import com.explorestack.iab.utils.IabClickCallback;
import com.explorestack.iab.utils.Utils;

import io.bidmachine.ContextProvider;
import io.bidmachine.unified.UnifiedFullscreenAdCallback;
import io.bidmachine.utils.BMError;

class MraidFullScreenAdListener implements MraidInterstitialListener {

    @NonNull
    private final ContextProvider contextProvider;
    @NonNull
    private final UnifiedFullscreenAdCallback callback;

    MraidFullScreenAdListener(@NonNull ContextProvider contextProvider,
                              @NonNull UnifiedFullscreenAdCallback callback) {
        this.contextProvider = contextProvider;
        this.callback = callback;
    }

    @Override
    public void onLoaded(@NonNull MraidInterstitial mraidInterstitial) {
        callback.onAdLoaded();
    }

    @Override
    public void onError(@NonNull MraidInterstitial mraidInterstitial, int i) {
        if (i == MraidError.SHOW_ERROR) {
            callback.onAdShowFailed(BMError.Internal);
        } else {
            callback.onAdLoadFailed(BMError.noFillError(null));
        }
    }

    @Override
    public void onShown(@NonNull MraidInterstitial mraidInterstitial) {
        callback.onAdShown();
    }

    @Override
    public void onOpenBrowser(@NonNull MraidInterstitial mraidInterstitial,
                              @NonNull String url,
                              @NonNull final IabClickCallback iabClickCallback) {
        callback.onAdClicked();

        Utils.openBrowser(contextProvider.getContext(), url, new Runnable() {
            @Override
            public void run() {
                iabClickCallback.clickHandled();
            }
        });
    }

    @Override
    public void onPlayVideo(@NonNull MraidInterstitial mraidInterstitial, @NonNull String s) {

    }

    @Override
    public void onClose(@NonNull MraidInterstitial mraidInterstitial) {
        callback.onAdFinished();
        callback.onAdClosed();
    }

}