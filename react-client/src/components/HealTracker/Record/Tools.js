import { arc, pie, selectAll, easeLinear, scaleLinear } from "d3";
import { useLayoutEffect, useContext } from "react";
import { Link } from "react-router-dom";
import { faInfoCircle } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { NutritionRecordContext } from "../../../context/NutritionRecordContext";

export function Gauge({ width, height, type }) {
  const { record } = useContext(NutritionRecordContext);
  const diameter = width / 2 + height / 2;
  const centerX = width / 2;
  const centerY = height / 2;

  useLayoutEffect(() => {
    selectAll(".pathEffect").transition().duration(2000).attr("fill", "red");
    selectAll(".text").transition().duration(2000).attr("fill", "black");
  });

  let averageValue = null;

  if (type === "Vitamin") {
    averageValue = record.vitaminAveragePrecented;
  } else if (type === "Electrolyte") {
    averageValue = record.electrolyteAveragePrecented;
  } else {
    averageValue = record.macronutrientAveragePrecented;
  }

  const pieData = pie()([averageValue, 100 - averageValue]);
  const foregroundArc = pieData[0];

  if (averageValue === null) {
    return <div id="preloader"></div>;
  }

  return (
    <div>
      <svg width={width} height={height}>
        <g transform={`translate(${centerX},${centerY})`}>
          <path
            d={arc()
              .innerRadius(diameter / 3.2)
              .outerRadius(diameter / 2.2)
              .startAngle(-0)
              .endAngle(-100)()}
            fill="gray"
          />

          <path
            className="pathEffect"
            d={arc()
              .innerRadius(diameter / 3.2)
              .outerRadius(diameter / 2.2)
              .startAngle(-foregroundArc.startAngle)
              .endAngle(-foregroundArc.endAngle)()}
            fill="gray"
          />

          <text
            className="text"
            transform={`translate(${0},${-10})`}
            textAnchor="middle"
            fill="#f0f0f0"
            fontSize={width / 12 + "px"}
          >
            {type}
          </text>
          <text
            className="text"
            fill="#f0f0f0"
            transform={`translate(${0},${+25})`}
            textAnchor="middle"
            fontSize={width / 12 + "px"}
          >
            {averageValue.toFixed(2) + "%"}
          </text>
        </g>
      </svg>
    </div>
  );
}

export function PipeChar({ width, height, data }) {
  const xScale = scaleLinear().domain([0, 100]).range([0, width]);

  useLayoutEffect(() => {
    selectAll(".rect")
      .transition()
      .ease(easeLinear)
      .duration(2000)
      .attr("width", xScale(data.precented));
  });

  return (
    <div
      className="WorkHere"
      width={width + 80}
      height={height + 60}
      style={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: "#EFEFEF",
        borderRadius: 15,
        overflow: "hidden",
        padding: 20,
      }}
    >
      <div style={{ display: "flex" }}>
        <span style={{ padding: 5, color: "#67C240" }}>{data.consumed}</span>
        <div
          style={{
            width: width,
            height: height,
            backgroundColor: "#67C240",
            borderRadius: 15,
            overflow: "hidden",
          }}
        >
          <svg style={{ width: "100%", height: "100%" }}>
            <rect
              className="rect"
              width={0}
              style={{
                height: "100%",
                fill: "red",
                stroke: "black",
              }}
            />
          </svg>
        </div>
        <span style={{ padding: 5, color: "#67C240" }}>{data.max}</span>
      </div>
      {data.consumed === 0 && (
        <div style={{ color: "#67C240", fontSize: 16, marginTop: 10 }}>
          Nothing is consumed !{" "}
          {data.type === "Vitamin" && (
            <Link to={"/nutrientInfo/vitamin/" + data.name}>
              {" "}
              <FontAwesomeIcon
                icon={faInfoCircle}
                style={{ fontSize: "24px", marginRight: "5px" }}
              />
            </Link>
          )}
          {data.type === "Electrolyte" && (
            <Link to={"/nutrientInfo/electrolyte/" + data.name}>
              {" "}
              <FontAwesomeIcon
                icon={faInfoCircle}
                style={{ fontSize: "24px", marginRight: "5px" }}
              />
            </Link>
          )}
          {data.type === "Macronutrient" && (
            <Link to={"/nutrientInfo/macronutrient/" + data.name}>
              {" "}
              <FontAwesomeIcon
                icon={faInfoCircle}
                style={{ fontSize: "24px", marginRight: "5px" }}
              />
            </Link>
          )}
        </div>
      )}
      {data.type === "Vitamin" && data.consumed !== 0 && (
        <div style={{ color: "#67C240", fontSize: 16, marginTop: 10 }}>
          Consumed {data.consumed}{" "}
          {data.measurement.match(/\(([^)]+)\)/)[1].toLowerCase()} of Vitamin{" "}
          {data.name}.
          <Link to={"/nutrientInfo/vitamin/" + data.name}>
            {" "}
            <FontAwesomeIcon
              icon={faInfoCircle}
              style={{ fontSize: "24px", marginRight: "5px" }}
            />
          </Link>
        </div>
      )}
      {data.type === "Electrolyte" && data.consumed !== 0 && (
        <div style={{ color: "#67C240", fontSize: 16, marginTop: 10 }}>
          Consumed {data.consumed}{" "}
          {data.measurement.match(/\(([^)]+)\)/)[1].toLowerCase()} of{" "}
          {data.name}.
        </div>
      )}
      {data.type === "Macronutrient" && data.consumed !== 0 && (
        <div style={{ color: "#67C240", fontSize: 16, marginTop: 10 }}>
          Consumed {data.consumed}{" "}
          {data.measurement.match(/\(([^)]+)\)/)[1].toLowerCase()} of{" "}
          {data.name}.
        </div>
      )}
    </div>
  );
}
