import React from "react";
import logo from "./onlinelogo.png";
import './HeaderLogo.css'
import LoginModal from "./LoginModal";
import 'bootstrap/js/dist/dropdown'


const Header = () => {
  
  return (
    <>
    <LoginModal/>
  <div className="sidebar-nav bg-secondary ">
    <nav className="navbar navbar-dark bg-secondary ">
      <div className="mx-2">
        <button className="navbar-toggler " type="button" data-bs-toggle="offcanvas"
        data-bs-target="#offcanvasNavbar" >
          <span className="navbar-toggler-icon"></span>
        </button>
        <img src={logo} alt="online-exam-logo" className="custom-logos " />
        <span className="" >ExamPortal</span>
        {/* admin offcanvass start */}

         <div className="bg-secondary offcanvas offcanvas-start " 
          id="offcanvasNavbar" >
          <div className="offcanvass-body">
            <ul className="navbar-nav">

              <li>
                <a href="#">
                <i class="bi bi-house text-white"></i>
                <span className="item-text text-light">Home</span>
                </a>
                </li>
            
                <li>
                <a href="#">
              <i className='fa fa-users'></i>
                <span className='bi bi-grid text-white'>Dashbord</span>
                </a>
                </li>

                <li>
                <a href="add-exam">
               <i className='bi bi-motherboard text-light'></i>
                <span className="item-text text-light">AddExam</span>
                </a>
               
                </li>

                <li>
                <a href="#">
              <i className='bi bi-blockquote-right text-light'></i>
                <span className="item-text text-light">ViewExam</span>
                </a>
                </li>
                <li>
                <a href="#">
              <i className='bi bi-motherboard text-light'></i>
                <span className="item-text text-light">AddTopic</span>
                </a>
                </li>

                <li>
                <a href="#">
              <i className='bi bi-blockquote-right text-light'></i>
                <span className="item-text text-light">ViewTopic</span>
                </a>
                </li>

                <li>
                <a href="add-question">
              <i className='bi bi-motherboard text-light'></i>
                <span className="item-text text-light">AddQuestion</span>
                </a>
                </li>

                <li>
                <a href="view-question">
              <i className='bi bi-blockquote-right text-light'></i>
                <span className="item-text text-light">ViewQuestion</span>
                </a>
                </li>

                <li>
                <a href="#">
              <i className='bi bi-people text-light'></i>
                <span className="item-text text-light">User</span>
                </a>
                </li>
                <li></li>
            </ul>
          

          </div>
         
        
          
         {/* admin offcanvas end */}
         </div>
      </div>
  
      <button
                    className="btn btn-light mx-4 "
                    data-bs-toggle="modal"
                    data-bs-target="#enroll"
                  >
                    Login
                  </button>
    </nav>
  </div>
    </>
  );
};

export default Header;