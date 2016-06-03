//package com.k.crashreport;
package com.ktpoc.tvcomm.consulting;

import android.app.Application;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

@ReportsCrashes(
        formUri = "http://183.98.91.175:5984/acra-genie_consult/_design/acra-storage/_update/report",
        reportType = HttpSender.Type.JSON,
        httpMethod = HttpSender.Method.PUT,
        formUriBasicAuthLogin = "report10",
        formUriBasicAuthPassword = "cr123",
        includeDropBoxSystemTags = true,
        dropboxCollectionMinutes = 3,          
        formKey = "", 
        customReportContent = {
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.PACKAGE_NAME,
                ReportField.REPORT_ID,
                ReportField.BUILD,
                ReportField.AVAILABLE_MEM_SIZE,
                ReportField.STACK_TRACE,
                ReportField.LOGCAT,
                ReportField.EVENTSLOG,
                ReportField.RADIOLOG,
                ReportField.DROPBOX
        },

        logcatArguments = { "-t", "500", "-v", "time", "*:D" }
)

public class CReport extends Application {
    @Override
    public final void onCreate() {
        super.onCreate();
        ACRA.init(this);
    }
}

