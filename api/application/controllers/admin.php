<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Admin extends CI_Controller {

	public function __construct()
	{
		parent::__construct();
	    $this->load->database();
	    $this->load->helper('url');
		$this->load->library('grocery_CRUD');
	}

	public function _setting_output($output = null)
	{
		$this->load->view('adminView.php',$output);
	}
	public function index()
	{

		$this->_setting_output();

	}
	public function user()
	{
		$crud = new grocery_CRUD();
		$crud->set_language("spanish");

		$crud->set_theme('twitter-bootstrap');

		//Spanish UI Translation
		$crud->set_subject('Miembro');
		$crud->display_as('gender',"Sexo")
			->display_as('name',"Nombre")
			->display_as('age',"Nascimiento")
			->display_as('hasDiabetes',"Diabético?")
			->display_as('role',"Funccíon")
			->display_as('isClinicasMember',"Miembro de Clinicas?")
			->display_as('accountId',"ClinicasId")
			->display_as('name',"Nombre");
		$crud->field_type('password', 'password');
		$output = $crud->render();
		$this->_setting_output($output);
	}
	public function categories()
	{
		$crud = new grocery_CRUD();
		$crud->set_theme('twitter-bootstrap');
		$crud->set_subject('Categories');
		$output = $crud->render();
		$this->_setting_output($output);
	}
	public function article()
	{
		$crud = new grocery_CRUD();
		$crud->set_theme('twitter-bootstrap');
		$crud->set_subject('Article');
		$output = $crud->render();

		$this->_setting_output($output);
	}

}