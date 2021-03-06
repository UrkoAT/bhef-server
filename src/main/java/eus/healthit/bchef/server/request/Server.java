package eus.healthit.bchef.server.request;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import eus.healthit.bchef.server.request.handlers.IngredientAPI;
import eus.healthit.bchef.server.request.handlers.Lumbra;
import eus.healthit.bchef.server.request.handlers.RecipeAPI;
import eus.healthit.bchef.server.request.handlers.SearchAPI;
import eus.healthit.bchef.server.request.handlers.UserAPI;

public class Server {
	public static void main(String[] args) {

		port(80);
		path("/api", () -> {

			before("/*", (req, res) -> {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date date = new Date();
				System.out.println("[" + dateFormat.format(date) + "] INFO Received API call" + req.pathInfo());
			});

			/* ############### GET ############### */
			get("/search", (req, res) -> SearchAPI.search(req, res));
			get("/byingredients", (req, res)-> SearchAPI.byIngredient(req, res));
			get("/ingredient", (req, res) -> IngredientAPI.ingredientLike(req, res));
			get("/page", (req, res) -> SearchAPI.page(req, res));

			/* ############### PUT ############### */
			put("/auth", (req, res) -> UserAPI.auth(req, res));
			path("/user", () -> {
				put("/follow", (req, res) -> UserAPI.follow(req, res));
				put("/unfollow", (req, res) -> UserAPI.unfollow(req, res));
				get("/name", (req, res) -> UserAPI.getName(req, res));
				get("/allusers", (req,res) -> UserAPI.getAllUsers(req, res));
				get("/history", (req, res) -> UserAPI.getHistoryBetween(req, res));
				get("/byId", (req, res) -> UserAPI.getById(req, res));
				put("/rate", (req, res) -> RecipeAPI.vote(req, res));
				put("/config", (req, res) -> UserAPI.userUpdate(req, res));
				put("/visit", (req, res) -> UserAPI.addHistory(req, res));
				put("/reauth", (req, res) -> UserAPI.reauth(req, res) );
				put("/save", (req, res) -> UserAPI.makeSavedRelation(req, res));
				put("/unsave", (req, res) -> UserAPI.removeSavedRelation(req, res));
				path("/shoplist", () -> {
					put("/add", (req, res) -> UserAPI.shoplistAdd(req, res));
					put("/remove", (req, res) -> UserAPI.shoplistRem(req, res));
					put("/tick", (req, res) -> UserAPI.shoplistTick(req, res));
				});
			});

			/* ############### POST ############### */
			path("/register", () -> {
				post("/user", (req, res) -> UserAPI.addUser(req, res));
				get("/check", (req, res) -> UserAPI.checkUser(req, res));
				post("/recipe", (req, res) -> RecipeAPI.addRecipe(req, res));
			});
			
			/*############## EASTER ############### */
			get("/lumbra", (req, res) -> Lumbra.lumbra());
			
		});
	}
}
