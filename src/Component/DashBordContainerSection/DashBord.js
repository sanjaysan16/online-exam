import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import useStateRef from "react-usestateref";
import { AppContext } from "../headerSectionContainer/Header";
import { port, protocol } from "../fetchConst";

const DashBord = () => {
  const [partyIdOfAdmin, setPartyIdOfAdmin, refPartyIdOfAdmin] = useStateRef()
  const uri = `${protocol}://${window.location.hostname}:${port}`
  const navigate = useNavigate();

  useEffect(() => {
    getPartyId()
  }, [])
  function getPartyId() {
    fetch(`${uri}/onlineexamapplication/control/get-partyid`, { credentials: "include" })
      .then(res => res.json())
      .then(data =>
        setPartyIdOfAdmin(data.partyId)
      )
  }

  return (
    <>
      <div className="row py-5 g-0">
        <div
          class="border col-sm-3 card p-0 m-5 offset-md-1 custom-bd-color"
          onClick={() => {
            navigate(`/view-user?partyIdOfAdmin=${refPartyIdOfAdmin.current}`);
          }}
        >
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

        <div
          class="border col-sm-3 card p-0 m-5 offset-md-1 custom-bd-color"
          onClick={() => {
            navigate(`/view-exam`);
          }}
        >
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
