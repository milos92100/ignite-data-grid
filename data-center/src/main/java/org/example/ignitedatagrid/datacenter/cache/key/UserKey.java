package org.example.ignitedatagrid.datacenter.cache.key;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;
import org.example.ignitedatagrid.domain.User;

public class UserKey {

    public static UserKey create(User user) {
        return new UserKey(user.getId(), user.getCompanyId());
    }

    private final long id;

    @AffinityKeyMapped
    private final long companyId;

    private UserKey(long id, long companyId) {
        this.id = id;
        this.companyId = companyId;
    }

    public long getId() {
        return id;
    }

    public long getCompanyId() {
        return companyId;
    }
}
