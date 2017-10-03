package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.matrix.Attribute;
import com.jim.multipos.data.db.model.matrix.AttributeType;
import com.jim.multipos.data.db.model.matrix.ChildAttribute;
import com.jim.multipos.data.db.model.matrix.ParentAttribute;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by DEV on 20.09.2017.
 */

public interface MatrixOptions {
    Observable<Long> addAttribute(Attribute attribute);
    Observable<Boolean> addAttributes(List<Attribute> attributes);
    Observable<List<Attribute>> getAllAttributes();
    Observable<Boolean> removeAttribute(Attribute attribute);
    Observable<Long> addAttributeType(AttributeType attributeType);
    Observable<Boolean> addAttributeTypes(List<AttributeType> attributeTypes);
    Observable<List<AttributeType>> getAllAttributeTypes();
    Observable<Boolean> removeAttributeTypes(AttributeType attributeType);
    Observable<Boolean> removeAttributeTypesByName(String name);
    Observable<Long> addChildAttribute(ChildAttribute childAttribute);
    Observable<Boolean> addChildAttributes(List<ChildAttribute> childAttributes);
    Observable<List<ChildAttribute>> getAllChildAttributes();
    Observable<Boolean> removeChildAttribute(ChildAttribute childAttribute);
    Observable<Long> addParentAttribute(ParentAttribute parentAttribute);
    Observable<Boolean> addParentAttributes(List<ParentAttribute> parentAttributes);
    Observable<List<ParentAttribute>> getAllParentAttributes();
    Observable<Boolean> removeParentAttribute(ParentAttribute parentAttribute);
}
