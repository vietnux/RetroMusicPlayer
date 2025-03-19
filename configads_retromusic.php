<?php
header("Access-Control-Allow-Origin: *");
echo '{
    "data":{"banner":"admob", "interstitial": "admob", "native":"admob",
	"begin":1,"nex_level":1,"end":0,"openapp": 1,
        "ad_units" :{
            "ios" : {
                "banner":"",
                "interstitial":"",
                "nativeAd":"",
		        "native_banner":""
            },
            "android" : {
                "banner":"",
                "interstitial":"",
                "nativeAd":"",
				"native_banner":""
            }
        },
        "admobid": {
            "android": {
                "banner": "",
                "interstitial": "",
                "rewardvideo": "",
		        "native":"",
		        "openapp":""
            },
            "ios" : {
                "banner": "",
                "interstitial": "",
                "rewardvideo": ""
            }
        }
    }
}';
?>