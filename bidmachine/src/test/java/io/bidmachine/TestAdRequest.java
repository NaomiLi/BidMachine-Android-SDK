package io.bidmachine;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.bidmachine.models.AuctionResult;
import io.bidmachine.models.DataRestrictions;
import io.bidmachine.unified.UnifiedAdRequestParams;

public class TestAdRequest extends AdRequest {

    private TestAdRequest(final Builder builder) {
        super(builder.adsType);
        auctionResult = new AuctionResult() {
            @NonNull
            @Override
            public String getId() {
                return builder.auctionId;
            }

            @Nullable
            @Override
            public String getDemandSource() {
                return builder.auctionDemandSource;
            }

            @Override
            public double getPrice() {
                return builder.auctionPrice;
            }

            @Nullable
            @Override
            public String getDeal() {
                return builder.auctionDeal;
            }

            @Override
            public String getSeat() {
                return builder.auctionSeat;
            }

            @NonNull
            @Override
            public String getCreativeId() {
                return builder.auctionCreativeId;
            }

            @Nullable
            @Override
            public String getCid() {
                return builder.auctionCid;
            }

            @Nullable
            @Override
            public String[] getAdDomains() {
                return builder.auctionAdDomains;
            }

            @NonNull
            @Override
            public String getNetworkKey() {
                return builder.auctionNetworkName;
            }

            @Nullable
            @Override
            public CreativeFormat getCreativeFormat() {
                return builder.auctionCreativeFormat;
            }

        };
    }

    @NonNull
    public AuctionResult getAuctionResult() {
        assert auctionResult != null;
        return auctionResult;
    }

    @NonNull
    @Override
    protected UnifiedAdRequestParams createUnifiedAdRequestParams(@NonNull TargetingParams targetingParams,
                                                                  @NonNull DataRestrictions dataRestrictions) {
        return null;
    }

    public static class Builder {

        AdsType adsType;
        String auctionId;
        String auctionDemandSource;
        double auctionPrice;
        String auctionDeal;
        String auctionSeat;
        String auctionCreativeId;
        String auctionCid;
        String[] auctionAdDomains;
        String auctionNetworkName = "test_network";
        CreativeFormat auctionCreativeFormat;

        public Builder(@NonNull AdsType adsType) {
            this.adsType = adsType;
        }

        public Builder setAuctionId(String auctionId) {
            this.auctionId = auctionId;
            return this;
        }

        public Builder setAuctionDemandSource(String auctionDemandSource) {
            this.auctionDemandSource = auctionDemandSource;
            return this;
        }

        public Builder setAuctionPrice(double auctionPrice) {
            this.auctionPrice = auctionPrice;
            return this;
        }

        public Builder setAuctionDeal(String auctionDeal) {
            this.auctionDeal = auctionDeal;
            return this;
        }

        public Builder setAuctionSeat(String auctionSeat) {
            this.auctionSeat = auctionSeat;
            return this;
        }

        public Builder setAuctionCreativeId(String auctionCreativeId) {
            this.auctionCreativeId = auctionCreativeId;
            return this;
        }

        public Builder setAuctionCid(String auctionCid) {
            this.auctionCid = auctionCid;
            return this;
        }

        public Builder setAuctionAdDomains(String[] auctionAdDomains) {
            this.auctionAdDomains = auctionAdDomains;
            return this;
        }

        public Builder setAuctionNetworkName(String auctionNetworkName) {
            this.auctionNetworkName = auctionNetworkName;
            return this;
        }

        public Builder setAuctionCreativeFormat(CreativeFormat auctionCreativeFormat) {
            this.auctionCreativeFormat = auctionCreativeFormat;
            return this;
        }

        public TestAdRequest build() {
            return new TestAdRequest(this);
        }

    }

}