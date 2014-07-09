<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

require(APPPATH.'/libraries/REST_Controller.php');

class clinicas_api extends REST_Controller {

    // Users HTTP verbs: GET POST PUT DELETE
    function users_get() {
        // Get 10 users if count is not specified
        // GET http://localhost/clinicas/index.php/clinicas_api/users
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
        $out = array_values($res);
        $data = $out;
        $this->response($data);
    } 
    function user_get() {
        // Gets user specified by id
        // GET http://localhost/clinicas/index.php/clinicas_api/user/id/2
        if(!$this->get('id'))
        {
            $this->response(NULL, 400);
        }
        $this->load->database();
        $res = $this->db->
                get_where('user', array('userId' => $this->get('id')), 1, 0)->
                result_array();
        $out = array_values($res);
        $data = $out;
        $this->response($data, 200);
    }
    function user_post() {
        // Updates the user specified by the userId and the other parameters
        // POST http://localhost/clinicas/index.php/clinicas_api/user/userId/2/name/Arjun/age/26
        // Response true or false

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
    function user_put() {
        // Creates a new user and respond with a status/errors
        // PUT localhost/clinicas/index.php/clinicas_api/user/
        // Parameters passed in the header
        // curl -X PUT -d name=Arjun -d age=25 localhost/clinicas/index.php/clinicas_api/user/
        if(!$this->put('name')) {
            $this->response(NULL, 400);
        }
        else 
        {
            $this->load->database();
            $res = $this->db->insert('user', $this->put());
            $this->response($res, 200);
        }
    }
    function user_delete() {
        // delete a user and respond with a status/errors
        // DELETE http://localhost/clinicas/index.php/clinicas_api/user/userId/2
        $this->load->database();
        $res = $this->db->delete('user', array('userId' => $this->get('userId')));
        $data = $res;
        $this->response($data);
    }

}
