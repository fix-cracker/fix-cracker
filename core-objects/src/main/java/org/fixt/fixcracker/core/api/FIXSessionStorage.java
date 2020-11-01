package org.fixt.fixcracker.core.api;

import org.fixt.fixcracker.core.domain.FIXSession;

import java.util.List;

public interface FIXSessionStorage {
    List<FIXSession> getAll() throws Exception;

    void add(FIXSession session) throws Exception;

    void update(FIXSession oldstate, FIXSession newState) throws Exception;

    void remove(FIXSession session) throws Exception;
}
