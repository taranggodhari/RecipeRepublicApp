package com.centennial.reciperepublic.myapplication;
// Authors:
//Akanksha Sarna (300932073)
//Tarang Godhari (300931365)
//Vrunda Shah (300900997)
//Yash Brahmbhatt (300932152)
public class EdamamModel {

    public class Rootobject {
        public String q;
        public int from;
        public int to;
        public Params _params;
        public boolean more;
        public int count;
        public Hit[] hits;

        public String getQ() {
            return q;
        }

        public void setQ(String q) {
            this.q = q;
        }

        public int getFrom() {
            return from;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public int getTo() {
            return to;
        }

        public void setTo(int to) {
            this.to = to;
        }

        public Params get_params() {
            return _params;
        }

        public void set_params(Params _params) {
            this._params = _params;
        }

        public boolean isMore() {
            return more;
        }

        public void setMore(boolean more) {
            this.more = more;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public Hit[] getHits() {
            return hits;
        }

        public void setHits(Hit[] hits) {
            this.hits = hits;
        }
    }

    public class Params {
        public Object[] sane;
        public String[] app_key;
        public String[] health;
        public String[] from;
        public String[] to;
        public String[] calories;
        public String[] app_id;

        public Object[] getSane() {
            return sane;
        }

        public void setSane(Object[] sane) {
            this.sane = sane;
        }

        public String[] getApp_key() {
            return app_key;
        }

        public void setApp_key(String[] app_key) {
            this.app_key = app_key;
        }

        public String[] getHealth() {
            return health;
        }

        public void setHealth(String[] health) {
            this.health = health;
        }

        public String[] getFrom() {
            return from;
        }

        public void setFrom(String[] from) {
            this.from = from;
        }

        public String[] getTo() {
            return to;
        }

        public void setTo(String[] to) {
            this.to = to;
        }

        public String[] getCalories() {
            return calories;
        }

        public void setCalories(String[] calories) {
            this.calories = calories;
        }

        public String[] getApp_id() {
            return app_id;
        }

        public void setApp_id(String[] app_id) {
            this.app_id = app_id;
        }
    }

    public class Hit {
        public Recipe recipe;
        public boolean bookmarked;
        public boolean bought;

        public Recipe getRecipe() {
            return recipe;
        }

        public void setRecipe(Recipe recipe) {
            this.recipe = recipe;
        }

        public boolean isBookmarked() {
            return bookmarked;
        }

        public void setBookmarked(boolean bookmarked) {
            this.bookmarked = bookmarked;
        }

        public boolean isBought() {
            return bought;
        }

        public void setBought(boolean bought) {
            this.bought = bought;
        }
    }

    public class Recipe {
        public String uri;
        public String label;
        public String image;
        public String source;
        public String url;
        public String shareAs;
        public float yield;
        public String[] dietLabels;
        public String[] healthLabels;
        public Object[] cautions;
        public String[] ingredientLines;
        public Ingredient[] ingredients;
        public float calories;
        public float totalWeight;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getShareAs() {
            return shareAs;
        }

        public void setShareAs(String shareAs) {
            this.shareAs = shareAs;
        }

        public float getYield() {
            return yield;
        }

        public void setYield(float yield) {
            this.yield = yield;
        }

        public String[] getDietLabels() {
            return dietLabels;
        }

        public void setDietLabels(String[] dietLabels) {
            this.dietLabels = dietLabels;
        }

        public String[] getHealthLabels() {
            return healthLabels;
        }

        public void setHealthLabels(String[] healthLabels) {
            this.healthLabels = healthLabels;
        }

        public Object[] getCautions() {
            return cautions;
        }

        public void setCautions(Object[] cautions) {
            this.cautions = cautions;
        }

        public String[] getIngredientLines() {
            return ingredientLines;
        }

        public void setIngredientLines(String[] ingredientLines) {
            this.ingredientLines = ingredientLines;
        }

        public Ingredient[] getIngredients() {
            return ingredients;
        }

        public void setIngredients(Ingredient[] ingredients) {
            this.ingredients = ingredients;
        }

        public float getCalories() {
            return calories;
        }

        public void setCalories(float calories) {
            this.calories = calories;
        }

        public float getTotalWeight() {
            return totalWeight;
        }

        public void setTotalWeight(float totalWeight) {
            this.totalWeight = totalWeight;
        }
    }


    public class Ingredient {
        public String text;
        public float weight;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public float getWeight() {
            return weight;
        }

        public void setWeight(float weight) {
            this.weight = weight;
        }
    }
}
