package com.jim.multipos.ui.product_queue_list.product_queue;

import com.jim.multipos.core.BasePresenterImpl;

import javax.inject.Inject;

public class ProductQueueListFragmentPresenterImpl extends BasePresenterImpl<ProductQueueListFragmentView> implements ProductQueueListFragmentPresenter {

    @Inject
    protected ProductQueueListFragmentPresenterImpl(ProductQueueListFragmentView productQueueListFragmentView) {
        super(productQueueListFragmentView);
    }
}
