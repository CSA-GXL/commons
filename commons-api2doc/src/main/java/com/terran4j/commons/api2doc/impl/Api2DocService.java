package com.terran4j.commons.api2doc.impl;

import com.terran4j.commons.api2doc.domain.ApiDocObject;
import com.terran4j.commons.api2doc.domain.ApiFolderObject;
import com.terran4j.commons.api2doc.other.utils.KeyedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Api2DocService {

    private static final Logger log = LoggerFactory.getLogger(Api2DocService.class);

    private static final String v = String.valueOf(System.currentTimeMillis());
	
	private final KeyedList<String, ApiFolderObject> folders = new KeyedList<>();
	
	public boolean hasFolder(String id) {
		return folders.containsKey(id);
	}
	
	public ApiFolderObject getFolder(String id) {
		return folders.get(id);
	}
	
	public void addFolder(ApiFolderObject folder) {
		if (folder == null) {
			throw new NullPointerException("ApiFolderObject is null");
		}
		folders.add(folder.getId(), folder);
	}
	
	public List<ApiFolderObject> getFolders() {
		return folders.getAll();
	}

    public String addAppDocVersion(String path) {
	    if (path.indexOf("?") > 0) {
	        return path + "&v=" + getAppDocVersion();
        } else {
            return path + "?v=" + getAppDocVersion();
        }
    }

    public String getAppDocVersion() {
        return v;
    }

    public String getComponentVersion() {
	    return v;
    }

    public ApiDocObject getDocObject(String folderId, String docId) throws Exception {
        ApiFolderObject folder = getFolder(folderId);
        if (folder == null) {
            log.warn("ApiFolder NOT Found: {}", folderId);
            return null;
        }

        ApiDocObject doc = folder.getDoc(docId);
        if (doc == null) {
            if (log.isWarnEnabled()) {
                log.warn("ApiDoc NOT Found: {}", folderId);
            }
            return null;
        }

        return doc;
    }

}
