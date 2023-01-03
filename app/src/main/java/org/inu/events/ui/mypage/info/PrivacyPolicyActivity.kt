package org.inu.events.ui.mypage.info

import org.inu.events.base.WebViewOnlyActivity

class PrivacyPolicyActivity : WebViewOnlyActivity() {
    override val toolbarTitle = "개인정보 처리방침"
    override val pageUrl =
        "https://raw.githubusercontent.com/inu-appcenter/terms-and-conditions/master/유니레터-개인정보처리방침.txt"
}