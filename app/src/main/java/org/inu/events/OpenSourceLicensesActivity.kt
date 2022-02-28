package org.inu.events

import org.inu.events.base.WebViewOnlyActivity

class OpenSourceLicensesActivity : WebViewOnlyActivity() {
    override val toolbarTitle = "오픈소스 라이센스"
    override val pageUrl =
        "https://raw.githubusercontent.com/inu-appcenter/terms-and-conditions/master/유니레터-개인정보처리방침"
}