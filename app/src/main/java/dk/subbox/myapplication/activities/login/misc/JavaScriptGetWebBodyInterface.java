package dk.subbox.myapplication.activities.login.misc;

import android.text.Html;
import android.util.Log;

public class JavaScriptGetWebBodyInterface
{
    @SuppressWarnings("unused")
    public String processHTML(final String html)
    {
        Log.i("processed html",html);

        String oAuthDetails;
        oAuthDetails= Html.fromHtml(html).toString();
        Log.i("oAuthDetails",oAuthDetails);
        return oAuthDetails;
    }
}