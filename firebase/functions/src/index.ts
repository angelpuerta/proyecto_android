import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

import * as express from 'express';
import * as engines from "consolidate";
import * as url from 'url';


admin.initializeApp();
const app = express();


app.engine('hbs', engines.handlebars);
app.set('views', ['./views/consulta', './views/formulario']);
app.set('view engine', 'hbs');
app.use(express.static('./public'));


app.use(function (req, res, next) {
    next();
});

app.get('/checkCode/:id/:code/:user', (req, res) => {
    const code = req.params.code;
    const id = req.params.id;
    const user = req.params.user;
    res.setHeader("Content-Type", "application/json");


    return admin.database().ref('events/' + id + '/code').once("value",
        (snapshot) => {
            const databaseCode = snapshot.val();
            if (databaseCode === code) {
                return admin.database().ref('usuarios').orderByChild('nick').equalTo(user).once("value", (snapshot2) => {

                    let userID = null;

                    snapshot2.forEach((child) => {
                        console.log(child.val());
                        if (child.key)
                            userID = child.key;
                        return true;
                    })

                    console.log(userID)
                    console.log(id)

                    if (userID === null) {
                        console.log("ENTERED THE DUNGEON");
                        const creatinguser = {};
                        creatinguser['nick'] = user;
                        creatinguser['assisted'] = {};
                        creatinguser['assisted'][id] = id;
                        console.log(creatinguser);
                        return admin.database().ref('usuarios').push(creatinguser).then(() => res.status(200).send("OK"), (e) => res.status(503).json(e));
                    }

                    return admin.database().ref('usuarios').child(userID).child('assisted').child(id).set(id).then(() => {
                        return admin.database().ref('assisted').child(id).child(userID).set(Date.now());
                    }).then(() => res.status(200).send("OK"), (e) => res.status(503).json(e));

                })

            }
            else
                res.status(401).json("Error");
            return null;
        })
        .catch(
            (e) => res.status(503).json(e)
        )

});

app.get('/checkUser/:user/:idEvent', (req, res) => {

    const assisted = { "qrVal": false, "commented": false };
    const idEvent = req.params.idEvent;
    const user = req.params.user;
    res.setHeader("Content-Type", "application/json");


    return new Promise(() => {
        admin.database().ref('usuarios').orderByChild('nick').equalTo(user).on("value", (snapshot) => {
            console.log(snapshot.val());
            if (snapshot.hasChild("assisted") && snapshot.child("assisted").hasChild(idEvent))
                assisted.qrVal = true;
            else
                assisted.qrVal = false;

            admin.database().ref('comments/' + idEvent).orderByChild("user").equalTo(user).once("value", (snapshot2) => {
                console.log(snapshot2.val());
                if (snapshot2.exists())
                    assisted.commented = true;
                else
                    assisted.commented = false;
            }).then(() => res.status(200).json(assisted), () => res.status(500).json(assisted));
        })

    }).then(() => res.status(200).json(assisted), () => res.status(500).json(assisted));

});


app.post('/addComment/', (req, res) => {


    const commentref = req.body.comment;

    console.log(req.body);

    const comment = {
        "id": commentref.c_id,
        "e_id": commentref.e_id,
        "comment": commentref.comment,
        "rate": commentref.rate,
        "timestamp": commentref.timestamp,
        "u_id": commentref.u_id
    }

    const code = req.body.code;

    Object.keys(comment).forEach(key => {
        if (comment[key] === undefined) {
            delete comment[key];
        }
    });

    console.log(comment);

    if (comment.id === undefined || comment.e_id === undefined || code === undefined) {
        res.status(400).send("Error params are not present")
        return null;
    }

    admin.database().ref('events/' + comment.e_id + '/code').once("value",
        (snapshot) => {
            const databaseCode = snapshot.val();
            if (databaseCode !== code) {
                res.status(401).json("Not valid code");
                return null;
            }
            else
                return admin.database().ref("comments/" + comment.e_id + "/").child(comment.id)
                    .set(comment)
                    .then(
                        () => res.status(200).send("Added"),
                        (error) => res.status(500).send(error)
                    );

        })
        .catch(
            (e) => res.status(503).json(e)
        )


});

app.get('/events/', (req, res) => {

    var events = [];

    return admin.database().ref('/events/').once("value")
        .then(snapshot => {
            snapshot.forEach((x) => {
                events.push({ 'url': getUrl(req)+"/"+x.val().id, 'tittle': x.val().tittle.toUpperCase() });
                return false;
            });
            console.log(hostUrl(req));
            res.render('events', { 'events': events, 'hostUrl':hostUrl(req)});
        })
});

function getUrl(req) {
    return url.format({
        protocol: req.protocol,
        host: req.get('host'),
        pathname: "qr"+req.originalUrl,
    });
}

function hostUrl(req){
    return url.format({
        protocol: req.protocol,
        host: req.get('host'),
        pathname: "qr"
    });
}

app.get('/events/:id/qr', (req, res) => {

    const id = req.params.id;
    res.setHeader("Content-Type", "text/html");

    return admin.database().ref('/events/' + id).child('code').once("value").then((snapshot) => {
        const code = snapshot.val();
        res.render('qr', { code });
    })
});

app.get('/events/:id', (req, res) => {

    const id = req.params.id;
    res.setHeader("Content-Type", "text/html");

    res.render('consulta', { id });
});

app.get('/addEvent', (req, res) => {
    res.setHeader("Content-Type", "text/html");

    res.render('formulario');
});

app.use(function (error, req, res, next) {
    console.log(error);
});

exports.qr = functions.https.onRequest(app);

exports.countComment = functions.database.ref('/comments/{eventid}/{commentid}').onWrite(
    async (change, context) => {

        const eventid = context.params.eventid;
        console.log(eventid);

        if ((change.after.exists() && change.before.exists()) || (!change.after.exists() && !change.before.exists())) {
            console.log("Nothing to do");
            return null;
        }

        return admin.database().ref('/events/' + eventid).once("value", function (datasnapshot) {
            const markref = datasnapshot.child('mark')
            const countref = datasnapshot.child('numberOfComments')
            let mark = markref.val() * countref.val();

            console.log(markref.val());
            console.log(countref.val());
            console.log(change.before.child('mark').val());

            if (change.after.exists() && !change.before.exists()) {
                console.log("ADDING");
                mark = mark + change.after.child('rate').val();
                const numberOfComments = countref.val() + 1
                mark = mark / numberOfComments
                console.log("FUTURE MARK " + mark);
                return datasnapshot.ref.update({ "mark": mark }).then(() => datasnapshot.ref.update({ "numberOfComments": numberOfComments }), () => console.log("ERROR AL AÑADIR"));


            } else if (!change.after.exists() && change.before.exists()) {
                console.log("DELETING");
                mark = mark - change.before.child('rate').val();
                const numberOfComments = countref.val() - 1
                if (numberOfComments !== 0)
                    mark = mark / numberOfComments;
                else
                    mark = 0;
                console.log("FUTURE MARK " + mark);
                return datasnapshot.ref.update({ "mark": mark }).then(() => datasnapshot.ref.update({ "numberOfComments": numberOfComments }), () => console.log("ERROR AL QUITAR"));

            } else {
                console.log("Nothing to do");
                return null;
            }

        });

    });