package com.jim.multipos.ui.product_queue_list;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

public class ProductQueueListPresenterImpl extends BasePresenterImpl<ProductQueueListView> implements ProductQueueListPresenter {

    @Inject
    protected ProductQueueListPresenterImpl(ProductQueueListView productQueueListView) {
        super(productQueueListView);
    }
}
