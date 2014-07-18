<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

require(APPPATH.'/libraries/REST_Controller.php');

class clinicas_api extends REST_Controller {

    // Users HTTP verbs: GET POST PUT DELETE

    // Gets user specified by userId
    // Test: curl http://clinicas.engr.scu.edu/index.php/clinicas_api/user/userId/4
    function user_get() {
	//$user = $this->input->get_request_header('Email',TRUE);
	//$passwd = $this->input->get_request_header('Password',TRUE);
	//$this->load->database();
        //$res = $this->db->
        //        get_where('user', array('email' => $user, 1, 0)->
        //        result_array();
        //$data = array_values($res);
	//if ($data['password'] == $passwd )   
	//	$this->response($data, 200); 
	//else {
	//		$this->response(array('status' => 'Invalid user credentials'), 400);
	//}
    }

    // Updates the user specified by the userId and the other parameters
    // Test: curl -X POST -d userId=4 -d age=26 http://clinicas.engr.scu.edu/index.php/clinicas_api/user/
    function user_post() {
        if(!$this->post('userId')) {
            $this->response(array('status' => 'You need to provide a UserId'), 400);
        } 
        else {
            $this->load->database();
            $this->db->where('userId', $this->post('userId'));
            $res = $this->db->update('user', $this->post());
            $this->response(array('status' => 'success'), 200);
            }
    }

    // Test: curl -X PUT -d name=Arjun -d age=25 http://clinicas.engr.scu.edu/index.php/clinicas_api/user/
    function user_put() {
        if(!$this->put()) {
            $this->response(NULL, 400);
        }
        else 
        {
            $this->load->database();
            $res = $this->db->insert('user', $this->put());
            $this->response($res, 200);
        }
    }

    // Deletes user and respond with a status/errors
    // DELETE http://clinicas.engr.scu.edu/index.php/clinicas_api/user/userId/2    
    function user_delete() {

        $this->load->database();
        $data = $this->db->delete('user', array('userId' => $this->get('userId')));
        $this->response($data);
    }

    // Articles HTTP verbs: GET

    // Get 10 articles if count is not specified. Articles content are reduced to 200 characters.
    // curl http://clinicas.engr.scu.edu/index.php/clinicas_api/articles/count/10
    // curl http://clinicas.engr.scu.edu/index.php/clinicas_api/articles/sort/latest
    // curl http://clinicas.engr.scu.edu/index.php/clinicas_api/articles/sort/top
    // curl http://clinicas.engr.scu.edu/index.php/clinicas_api/articles/sort/favorites/userId/4
    // curl http://clinicas.engr.scu.edu/index.php/clinicas_api/articles/category/4
    function articles_get() {
        $this->load->database();
        if($this->get('sort') == "latest"){
		$this->db->order_by("date", "desc");
	} else if ($this->get('sort') == "top") {
		$this->db->select("article.*, count(favorites.articleId) as count");
		$this->db->order_by("date", "desc");
		$this->db->group_by("articleId");
		$this->db->join('favorites', 'article.articleId = favorites.articleId', 'inner');
	} else if ($this->get('sort') == "favorites") {
		$this->db->select("article.*, favorites.userId as user");
                $this->db->order_by("date", "desc");
                $this->db->group_by("userId");
                $this->db->join('favorites', 'article.articleId = favorites.articleId', 'inner');
		$this->db->where('favorites.userId',$this->get('userId'));
	}
	if($this->get('sort') == "category"){
		$this->db->where('categoryId',$this->get('category'));
	}

	if(!$this->get('count')) {
            $res = $this->db->
                    get('article', 10, 0)->
                    result_array();            
        } 
        else {
            $res = $this->db->
                    get('article', $this->get('count'), 0)->
                    result_array();
        }
        $data = array_values($res);
        foreach ($data as $articleNumber => $article) {
            $data[$articleNumber]['content'] = substr($article['content'], 0, 200);            
        }
        $this->response($data);
    } 

    // Gets article specified by articleId
    // Test: curl http://clinicas.engr.scu.edu/index.php/clinicas_api/article/articleId/4
    function article_get() {
        if(!$this->get('articleId'))
        {
            $this->response(NULL, 400);
        }
        $this->load->database();
        $res = $this->db->
                get_where('article', array('articleId' => $this->get('articleId')), 1, 0)->
                result_array();
        $data = array_values($res);
        $this->response($data, 200);
    }

    // Review HTTP verbs: GET PUT DELETE

    // Get 10 reviews if count is not specified.
    // curl http://clinicas.engr.scu.edu/index.php/clinicas_api/reviews/count/9
    function reviews_get() {
        $this->load->database();
        if(!$this->get('count')) {
            $res = $this->db->
                    get('review', 10, 0)->
                    result_array();            
        } 
        else {
            $res = $this->db->
                    get('review', $this->get('count'), 0)->
                    result_array();
        }
        $data = array_values($res);
        $this->response($data);
    } 

    // Creates a new review and respond with a status/errors
    // curl -X PUT -d userId=4 -d articleId=1 -d rating=4 -d comments="Interesting article" http://clinicas.engr.scu.edu/index.php/clinicas_api/review/
    function review_put() {
        if(!$this->put()) {
            $this->response(NULL, 400);
        }
        else 
        {
            $this->load->database();
            $res = $this->db->insert('review', $this->put());
            $this->response($res, 200);
        }
    }

    // Deletes review and respond with a status/errors
    // curl -X DELETE http://clinicasindex.php/clinicas_api/review/reviewId/2    
    function review_delete() {
        $this->load->database();
        $data = $this->db->delete('review', array('reviewId' => $this->get('reviewId')));
        $this->response($data);
    }

    // Feedback HTTP verbs: PUT

    // Creates a new feedback and respond with a status/errors
    // curl -X PUT -d type=1 -d message="I found this article..." -d userId=4 -d url="www.google.com" http://clinicas.engr.scu.edu/index.php/clinicas_api/feedback/
    function feedback_put() {
        if(!$this->put()) {
            $this->response(NULL, 400);
        }
        else 
        {
            $this->load->database();
            $res = $this->db->insert('feedback', $this->put());
            $this->response($res, 200);
        }
    }

    // Favorites HTTP verbs: GET PUT DELETE

    // Get all favorites for a specific userId.
    // curl http://clinicas.engr.scu.edu/index.php/clinicas_api/favorites/userId/4
    function favorites_get() {
        $this->load->database();
        if(!$this->get('userId')) {
            $this->response(NULL, 400);
        } 
        else {
            $res = $this->db->
                    get('favorites', $this->get('userId'), 0)->
                    result_array();
        }
        $data = array_values($res);
        $this->response($data);
    } 

    // Creates a new favorite and respond with a status/errors
    // curl -X PUT -d userId=4 -d articleId=1 http://clinicas.engr.scu.edu/index.php/clinicas_api/favorite/
    function favorite_put() {
        if(!$this->put()) {
            $this->response(NULL, 400);
        }
        else 
        {
            $this->load->database();
            $res = $this->db->insert('favorites', $this->put());
            $this->response($res, 200);
        }
    }

    // Deletes favorites and respond with a status/errors
    // curl -X DELETE http://clinicas.engr.scu.edu/index.php/clinicas_api/favorite/articleId/1/userId/4
    function favorite_delete() {
        $this->load->database();
        $data = $this->db->delete('favorites', array('userId' => $this->get('userId'), 'articleId' => $this->get('articleId') ));
        $this->response($data);
    }

    // Categories HTTP verbs: GET
    // Get all categories.
    // curl http://clinicas.engr.scu.edu/index.php/clinicas_api/categories/
    function categories_get() {
        $this->load->database();
            $res = $this->db->
                    get('categories')->
                    result_array();
        $data = array_values($res);
        $this->response($data);
    } 

}
