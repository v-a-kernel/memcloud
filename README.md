# memcloud

## how to build & run & test

	mvn clean package -Dmaven.test.skip=true -Pqa
	mvn jetty:run -Djetty.port=10008
	curl -x 10.213.57.166:10008 "http://passport.ffan.net/passport/session-create.json?loginid=chinaliwee@163.com&pwd=123456" -i
	curl -x 10.213.57.166:10008 "http://passport.ffan.net/passport/account-lookup.json?type=map&uids=2,1ueIUbXoMNQ." -i
	
	
	