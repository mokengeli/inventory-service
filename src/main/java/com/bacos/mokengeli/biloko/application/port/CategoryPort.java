package com.bacos.mokengeli.biloko.application.port;

import com.bacos.mokengeli.biloko.application.domain.DomainCategory;
import com.bacos.mokengeli.biloko.application.exception.ServiceException;

public interface CategoryPort {
    DomainCategory addCategory(DomainCategory category) throws ServiceException;
}
