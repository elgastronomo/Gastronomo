/**
 * Saves the notification inside LocalStorage to share them between pages
 */

function saveNotification(notification) {
    if (notification.comment) {
        if (localStorage.comments) {
            let c = JSON.parse(localStorage.comments);
            c.push(notification);
            localStorage.comments = JSON.stringify(c);
        } else {
            localStorage.comments = JSON.stringify([notification]);
        }
    }
    else if (notification.favRecipe) {
    	if (localStorage.likes) {
            let c = JSON.parse(localStorage.likes);
            c.push(notification);
            localStorage.likes = JSON.stringify(c);
        } else {
            localStorage.likes = JSON.stringify([notification]);
        }
    }
}

/**
 * 
 * Retrieve the notifications from the LocalStorage
 */
function loadNotifications() {
    if (localStorage.comments) {
        let c = JSON.parse(localStorage.comments);

        c.forEach((comment) => {
            handleMessage(comment);
        });
    }
    else if (localStorage.likes) {
    	 let c = JSON.parse(localStorage.likes);

         c.forEach((like) => {
             handleMessage(like);
         });
    }
}

/**
 * Removes a comment from the LocalStorage
 */
function removeCommentNotification(index) {
	if (index != -1) {
		let c = JSON.parse(localStorage.comments);
		c.splice(index, 1);
		localStorage.comments = JSON.stringify(c);
	}
}

/**
 * Removes a like from the LocalStorage
 */
function removeLikeNotification(index) {
	if (index != -1) {
		let c = JSON.parse(localStorage.likes);
		c.splice(index, 1);
		localStorage.likes = JSON.stringify(c);
	}
	
}


const handleMessage = (o) => {
    console.log(o);
    if (o.comment) {
        let not = `
        	<li class="collection-item avatar"><img
					src="/user/` + o.comment.user.id + `/photo" alt=""
					class="circle"> 
					<a href="/receta/` + o.comment.recipe.id + `/">
					<span class="title">¡Nuevo comentario en ` + o.comment.recipe.name + `!</span></a>
					<p>` + o.comment.user.name + ` ha comentado en una de tus recetas</p> 
					
					<a href="#!" class="secondary-content cancel-notifications"><i
						class="material-icons">done</i></a></li>
						`;
        $("#notifications").append(not);
        
        let count = $("#notifications-count").html();
        $("#notifications-count").html(++count);
        
        $('.cancel-notifications').click((elem) => {
        	let notification = $(elem.target).parent().parent();
        	let index = $("#notifications").children().index(notification);
        	notification.remove();
        	removeCommentNotification(index);
        	
        	if (index != -1) {
        		let count = $("#notifications-count").html();
                $("#notifications-count").html(--count);
        	}
        });
    }
    else if (o.favRecipe) {
    	let not = `
        	<li class="collection-item avatar"><img
					src="/user/` + o.user.id + `/photo" alt=""
					class="circle"> 
					<a href="/receta/` + o.favRecipe.id + `/">
					<span class="title">¡Nuevo me gusta en ` + o.favRecipe.name + `!</span></a>
					<p>A ` + o.user.name + ` le gusta una de tus recetas</p> 
					
					<a href="#!" class="secondary-content cancel-notifications"><i
						class="material-icons">done</i></a></li>
						`;
        $("#notifications").append(not);
        
        let count = $("#notifications-count").html();
        $("#notifications-count").html(++count);
        
        $('.cancel-notifications').click((elem) => {
        	let notification = $(elem.target).parent().parent();
        	let index = $("#notifications").children().index(notification);
        	notification.remove();
        	removeLikeNotification(index);
        	
        	if (index != -1) {
	        	let count = $("#notifications-count").html();
	            $("#notifications-count").html(--count);
        	}
        });
    }
}



/**
 * WebSocket API, which only works once initialized
 */
const ws = {

    /**
     * WebSocket, or null if none connected
     */
    socket: null,

    /**
     * Sends a string to the server via the websocket.
     * 
     * @param {string}
     *            text to send
     * @returns nothing
     */
    send: (text) => {
        if (ws.socket != null) {
            ws.socket.send(text);
        }
    },

    /**
     * Default action when text is received.
     * 
     * @returns
     */
    receive: (text) => {
        console.log(text);
    },

    /**
     * Attempts to establish communication with the specified web-socket
     * endpoint. If successfull, will call
     * 
     * @returns
     */
    initialize: (endpoint) => {
        try {
            ws.socket = new WebSocket(endpoint);
            ws.socket.onmessage = (e) => ws.receive(e.data);
            console.log("Connected to WS '" + endpoint + "'")
        } catch (e) {
            console.log("Error, connection to WS '" + endpoint + "' FAILED: ", e);
        }
    }
}


/**
 * Actions to perform once the page is fully loaded
 */
window.addEventListener('load', () => {
    loadNotifications();

    if (gastronomo.socketUrl != 'false') {
        ws.initialize(gastronomo.socketUrl);

        ws.receive = (text) => {
            console.log("just in:", text);
            try {
                const o = JSON.parse(text);
                handleMessage(o);
                saveNotification(o);
            } catch (e) {
                console.log("...not json: ", e);
            }
        }
    }

});