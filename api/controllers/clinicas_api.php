<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

require(APPPATH.'/libraries/REST_Controller.php');

class clinicas_api extends REST_Controller {

    // Users HTTP verbs: GET POST PUT DELETE

    // Get 10 users if count is not specified
    // Test: curl localhost/clinicas/index.php/clinicas_api/users/
    function users_get() {
        $this->load->database();
        if(!$this->get('count')) {
            $res = $this->db->
                    get('user', 10, 0)->
                    result_array();            
        } 
        else {
            $res = $this->db->
                    get('user', $this->get('count'), 0)->
                    result_array();
        }
        $data = array_values($res);
        $this->response($data);
    } 

    // Gets user specified by userId
    // Test: curl localhost/clinicas/index.php/clinicas_api/user/userId/4
    function user_get() {
    
        if(!$this->get('userId'))
        {
            $this->response(NULL, 400);
        }
        $this->load->database();
        $res = $this->db->
                get_where('user', array('userId' => $this->get('userId')), 1, 0)->
                result_array();
        $out = array_values($res);
        $data = $out;
        $this->response($data, 200);
    }

    // Updates the user specified by the userId and the other parameters
    // Test: curl -X POST -d userId=4 -d age=26 localhost/clinicas/index.php/clinicas_api/user/
    function user_post() {

        if(!$this->get('userId')) {
            $this->response(NULL, 400);
        } 
        else {
            $this->load->database();
            $this->db->where('userId', $this->get('userId'));
            $res = $this->db->update('user', $this->get());
            $this->response($res, 200);
            }
    }

    // Creates a new user and respond with a status/errors
    // Test: curl -X PUT -d name=Arjun -d age=25 localhost/clinicas/index.php/clinicas_api/user/
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
    // DELETE http://localhost/clinicas/index.php/clinicas_api/user/userId/2    
    function user_delete() {

        $this->load->database();
        $data = $this->db->delete('user', array('userId' => $this->get('userId')));
        $this->response($data);
    }

    // Articles HTTP verbs: GET

    // Get 10 articles if count is not specified. Articles content are reduced to 200 characters.
    // curl http://localhost/clinicas/index.php/clinicas_api/articles/count/10
    function articles_get() {
        $this->load->database();
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

    // Gets user specified by userId
    // Test: curl localhost/clinicas/index.php/clinicas_api/article/articleId/4
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

    // Get 10 reviews if count is not specified by articleId.
    // curl http://localhost/clinicas/index.php/clinicas_api/reviews/count/10
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
    // Test: curl -X PUT -d userId=4 -d articleId=1 -d rating=4 -d comments="Interesting article" http://localhost/clinicas/index.php/clinicas_api/review/
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
    // curl -X DELETE http://localhost/clinicas/index.php/clinicas_api/review/reviewId/2    
    function review_delete() {
        $this->load->database();
        $data = $this->db->delete('review', array('reviewId' => $this->get('reviewId')));
        $this->response($data);
    }

}
