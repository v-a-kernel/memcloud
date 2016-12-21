package com.sohu.tv.memcloud.mongodb;


import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;

public class MongodbFactory {

	private static DB db = null;
	private static MongodbFactory factory= null;

	public MongodbFactory(){
	}

	public static MongodbFactory getInstance(){
		if(factory == null){
			synchronized (MongodbFactory.class) {
				if(factory == null){
					factory = new MongodbFactory("10.11.132.83", 27017 ,"memstat");
				}
			}
		}
		return factory;
	}

	public MongodbFactory(String host,int port,String dbname) {
		this(host, port, dbname, null, null);
	}

	public MongodbFactory(String host,int port,String dbname, String user, String pwd) {
        try {
            ServerAddress addr = new ServerAddress(host, port);
            MongoOptions options = new MongoOptions();//REFER: http://api.mongodb.org/java/2.7.3/com/mongodb/MongoOptions.html
            Mongo mongo = new Mongo(addr,options);
            db = mongo.getDB(dbname);
            if (user!=null && pwd!=null) {
                boolean authed = db.authenticate(user, pwd.toCharArray());
                if (! authed ) {
                    throw new IllegalArgumentException("Mongodb User Or passowrd Wrong");
                }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

	public DB getMongoDB() {
		return db;
	}

	public DBCollection getDBCollection(String collName){
		return db.getCollection(collName);
	}
}
