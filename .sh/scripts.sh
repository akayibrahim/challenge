killall mongod

"/Users/iakay/software tools/mongodb/bin/mongod"

"/broker/bin/artemis" run

"/Users/iakay/software tools/mongodb/bin/mongo"

http://localhost:8080/downloadImage?challengeId=5b2cdaf41cb199833bc830ca&memberId=5b1a97bbcb353e79ca335a38

> help
        db.help()                    help on db methods
        db.mycoll.help()             help on collection methods
        sh.help()                    sharding helpers
        rs.help()                    replica set helpers
        help admin                   administrative help
        help connect                 connecting to a db help
        help keys                    key shortcuts
        help misc                    misc things to know
        help mr                      mapreduce

        show dbs                     show database names
        show collections             show collections in current database
        show users                   show users in current database
        show profile                 show most recent system.profile entries with time >= 1ms
        show logs                    show the accessible logger names
        show log [name]              prints out the last segment of log in memory, 'global' is default
        use <db_name>                set current database
        db.foo.find()                list objects in collection foo
        db.foo.find( { a : 1 } )     list objects in foo where a == 1
        it                           result of the last line evaluated; use to further iterate
        DBQuery.shellBatchSize = x   set default number of items to display on shell
        exit                         quit the mongo shell

commands
    use chl
    show collections

    db.member.find()
    db.member.remove({})
    db.member.update( { "_id": ObjectId("5b3152821cb199f1fadc0fab") }, { $set: {"privateMember": false} } )
    db.member.remove( { "_id": ObjectId("5b3152701cb199f1fadc0faa") })

    db.joinAttendance.find()
    db.joinAttendance.remove({})
    db.joinAttendance.update( { "_id": ObjectId("5b1fb4a21cb19924cc638840") }, { $set: {"join": true, "proof": false} } )
    db.joinAttendance.remove( { "_id": ObjectId("5b39b3c11cb1997e50a35cda") })

    db.versusAttendance.find()
    db.versusAttendance.remove({})
    db.versusAttendance.update( { "_id": ObjectId("5b1fb4a21cb19924cc638840") }, { $set: {"join": true, "proof": false} } )
    db.versusAttendance.remove( { "challengeId": "5b3e310c1cb1995e53e42a63" })

    db.challenge.find()
    db.challenge.find({ "challengerId": "5b3152d31cb199f1fadc0fb0" })
    db.challenge.remove({})
    db.challenge.update( { "done": true }, { $set: {"visibility": "1"} } )
    db.challenge.remove( { "_id": ObjectId("5b3e30651cb1995e53e42a5e") })
    db.challenge.find({"untilDate": {"$lt": new Date()} })
    db.challenge.find({ '$or' : [ { '$or' : [{'challengerId' : {$in : ['5b3152701cb199f1fadc0faa']} }, {'type' : 'PUBLIC'} ], 'deleted': {$in: [null, false]}, 'dateOfUntil': {'$gte': new Date()}, 'done': false }, { '$or' : [{'challengerId' : {$in : ['5b3152701cb199f1fadc0faa']} }, {'type' : 'PUBLIC'} ], 'deleted': {$in: [null, false]}, 'done': true } ] })

    db.activity.find()
    db.activity.remove({})
    db.activity.update( { "_id": ObjectId("5b1fb4a21cb19924cc63883d") }, { $set: {"proofed": false} } )
    db.activity.remove( { "_id": ObjectId("5b37be761cb19960037e831e") })

    db.notification.find()
    db.notification.remove({})
    db.notification.update( { "_id": ObjectId("5b1fb4a21cb19924cc63883d") }, { $set: {"proofed": false} } )
    db.notification.remove( { "_id": ObjectId("5b37be761cb19960037e831e") })

    db.friendList.find({ "memberId": "5b3152821cb199f1fadc0fab" })
    db.friendList.remove({ "friendMemberId": "5b36283d1cb199413144407e" })
    db.friendList.update( { "_id": ObjectId("5b1fb4a21cb19924cc63883d") }, { $set: {"proofed": false} } )
    db.friendList.remove( { "friendMemberId": "5b3152701cb199f1fadc0faa" })

    db.trendChallenge.find()
    db.trendChallenge.remove({})
    db.trendChallenge.update( { "_id": ObjectId("5b1fb4a21cb19924cc63883d") }, { $set: {"proofed": false} } )
    db.trendChallenge.remove( { "_id": ObjectId("5b39b3c11cb1997e50a35cd8") })

    db.error.find()
    db.error.remove({})
    db.error.update( { "_id": ObjectId("5b1fb4a21cb19924cc638840") }, { $set: {"join": true, "proof": false} } )
    db.error.remove( { "_id": ObjectId("5b363e4e1cb1994c04001cf4") })

    db.activityCount.find()
    db.activityCount.remove({})
    db.activityCount.update( { "memberId": "5b32959a1cb19909e464f6f5" }, { $set: {"count": "1"} } )
    db.activityCount.remove( { "_id": ObjectId("5b363e4e1cb1994c04001cf4") })

    db.fs.files.find()
    db.fs.files.find({ "metadata.challengeId": "5b4870f28d39663e146975de" })
    db.fs.files.remove({})
    db.fs.files.update( { "memberId": "5b32959a1cb19909e464f6f5" }, { $set: {"count": "1"} } )
    db.fs.files.remove( { "_id": ObjectId("5b363e4e1cb1994c04001cf4") })