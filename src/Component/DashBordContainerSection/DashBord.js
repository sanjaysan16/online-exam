import React, { useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import useStateRef from "react-usestateref";
import { AppContext } from "../headerSectionContainer/Header";

const DashBord = () => {
    const partyId=useContext(AppContext);
  const navigate = useNavigate();

 console.log(partyId,"hhhhhhhh")
  return (
    <>
      <div className="row mt-3 ">
        <div
          class="border col-sm-3 card p-0 m-5 offset-md-1 custom-bd-color" onClick={()=>{navigate(`/view-user`)}}>
          <div class="card-header  bg-dark text-white  fs-1 fw-bolder">
            <h4>
              <span className="bi bi-person-square mx-2"></span>User
            </h4>
          </div>
          <div className="card-body dashbord-card-body body-hover ">
            <h2>
              <span className="bi bi-person px-2"></span> Users
            </h2>
          </div>
        </div>

        <div class="border col-sm-3 card p-0 m-5 offset-md-1 custom-bd-color" onClick={()=>{navigate(`/view-exam`)}}>
          <div class="card-header bg-dark text-white  fs-1 fw-bolder ">
            <h4>
              <span className="bi bi-motherboard mx-2"></span>Exam
            </h4>
          </div>
          <div className="card-body body-hover">
            <h2>
              <span className="bi bi-journals px-2"></span> Exams{" "}
            </h2>
          </div>
        </div>
      </div>
    </>
  );
};

export default DashBord;
