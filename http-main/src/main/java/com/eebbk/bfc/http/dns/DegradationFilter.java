package com.eebbk.bfc.http.dns;

import android.support.annotation.NonNull;

public interface DegradationFilter {

	boolean shouldDegradeHttpDNS(@NonNull String hostName);

}
