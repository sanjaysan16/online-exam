import React from "react";
import logo from "./onlinelogo.png";
import './HeaderLogo.css'
import LoginModal from "./LoginModal";
import 'bootstrap/js/dist/dropdown'
import { useEffect, useState } from "react";
import useStateRef from "react-usestateref";




const Header = () => {

  
  const [name, setName] = useState('')
  const [flag, setFlag,refSetFlag] = useStateRef()
  const [showLogout, setShowLogout] = useState()
  const [partyIdOfAdmin, setPartyIdOfAdmin, partyIdOfAdminRef] = useStateRef()
  const trigger = () => {
    fetch(`https://localhost:8443/onlineexamapplication/control/user-login`,
      { credentials: "include" }
    )
      .then(response => response.json())
      .then(data => {
        console.log(data)
        const responseName = data.userFirstName
      setFlag(data.isUserlogin)
       
        setPartyIdOfAdmin(data.partyId)
        console.log(partyIdOfAdmin);
        setName(responseName);
       
        console.log("this is flag ", flag);
      })
      .catch(error => console.error('Error:', error))
  }
  async function logout() {
    console.log("This is logout fetch");
    const logoutFetch = await fetch("https://localhost:8443/onlineexamapplication/control/user-logout", { credentials: "include" });
    const logoutResponse = await logoutFetch.json();
    console.log(logoutResponse.isUserLogin);
    if (logoutResponse.isUserLogin === true) {
      setFlag(logoutResponse.isUserLogin);
      window.location.href = '/';
      //window.location.reload();
    }
  }
  useEffect(
    () => {
      trigger()
    }, []
  )
  
  return (
    <>
    <LoginModal/>
  <div className="sidebar-nav nav-color ">
    <nav className="navbar navbar-dark nav-color ">
      <div className="mx-2">
        <button className="navbar-toggler " type="button" data-bs-toggle="offcanvas"
        data-bs-target="#offcanvasNavbar" >
          <span className="navbar-toggler-icon"></span>
        </button>
       <a href='/'> <img src={logo} alt="online-exam-logo" className="custom-logos " /></a>
        <span className="" >ExamPortal</span>
        {/* admin offcanvass start */}

         <div className="nav-color offcanvas offcanvas-start " 
          id="offcanvasNavbar" >
          <div className="offcanvass-body">
            <ul className="navbar-nav">

              <li>
                <a href="/">
                <i className="bi bi-house text-white"></i>
                <span className="item-text text-light">Home</span>
                </a>
                </li>
            
                <li>
                <a href="admin-view">
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
                <a href="view-exam">
              <i className='bi bi-blockquote-right text-light'></i>
                <span className="item-text text-light">ViewExam</span>
                </a>
                </li>


                <li>
                <a href={`view-user?partyIdOfAdmin=${partyIdOfAdminRef.current}`}>
              <i className='bi bi-people text-light'></i>
                <span className="item-text text-light">User</span>
                </a>
                </li>
               
            </ul>
          

          </div>
         
        
          
         {/* admin offcanvas end */}
         </div>
      </div>
      <div className="col-md-4 col-sm-12 text-center">
                {refSetFlag.current ? (
                  <center>
                    <button
                      className="btn btn-light btn-lg"
                      data-bs-toggle="modal"
                      data-bs-target="#enroll"
                    >
                      Login
                    </button>
                  </center>
                ) : (<div className="text-light">welcome {name}
                  <button className="btn btn-light" onClick={() => logout()}>logout</button>
                </div>)}
              </div>
    </nav>
  </div>
    </>
  );
};

export default Header;