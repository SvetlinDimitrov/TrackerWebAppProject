import { faInfoCircle } from "@fortawesome/free-solid-svg-icons";
import chroma from "chroma-js";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  arc,
  axisBottom,
  axisLeft,
  easeLinear,
  pie,
  scaleBand,
  scaleLinear,
  select,
  selectAll,
  interpolate,
} from "d3";
import { useLayoutEffect, useRef, useState, useEffect , useMemo } from "react";
import { Link } from "react-router-dom";
import { calcAverageValue } from "./RecordUtils";

export function Gauge({ width, height, type, data, className }) {
  const diameter = width / 2 + height / 2;
  const centerX = width / 2;
  const centerY = height / 2;
  const textRef = useRef();

  let averageValue = null;

  if (type === "Vitamin") {
    averageValue = calcAverageValue(data, type);
  } else if (type === "Mineral") {
    averageValue = calcAverageValue(data, type);
  } else if (type === "Macronutrient") {
    averageValue = calcAverageValue(data, type);
  } else {
    averageValue =
      (data.dailyConsumedCalories / data.dailyCaloriesToConsume) * 100;
  }

  const pieData = pie()([averageValue, 100 - averageValue]);
  const foregroundArc = pieData[0];

  useLayoutEffect(() => {
    const percentage = averageValue / 100;
    const endAngle =
      percentage * (foregroundArc.endAngle - foregroundArc.startAngle);

    const arcGenerator = arc()
      .innerRadius(diameter / 3.2)
      .outerRadius(diameter / 2.2)
      .startAngle(0);

    const path = select(`.${className}`)
      .attr("d", arcGenerator.endAngle(-foregroundArc.endAngle)())
      .attr("fill", "#78909C");

    path
      .transition()
      .duration(4000) // duration of the transition in milliseconds
      .attrTween("d", function () {
        const i = interpolate(0, endAngle);
        return function (t) {
          return arcGenerator.endAngle(i(t))();
        };
      });
    const text = select(textRef.current);

    text
      .transition()
      .duration(4000) // duration of the transition in milliseconds
      .tween("text", function () {
        const i = interpolate(0, averageValue);
        return function (t) {
          this.textContent = `${i(t).toFixed(2)}%`;
        };
      });
  }, [
    diameter,
    averageValue,
    foregroundArc.endAngle,
    foregroundArc.startAngle,
    className,
  ]);

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

          <path className={className} fill="gray" />

          <text
            className="text"
            transform={`translate(${0},${-10})`}
            textAnchor="middle"
            fill="#CFD8DC" // light blue gray
            fontSize={width / 12 + "px"} // increase font size
            fontWeight="bold" // make font bold
          >
            {type}
          </text>
          <text
            ref={textRef}
            className="text"
            fill="#CFD8DC" // light blue gray
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
  const [currentColor, setCurrentColor] = useState("#67C240");

  useLayoutEffect(() => {
    const fills = Math.ceil(data.consumed / data.max);
    const durationPerFill = 4000 / fills;

    const colors = generateColors("#67C240", fills + 1);

    let transition = selectAll(".rect")
      .attr("fill", colors[1])
      .attr("stroke", "#455A64");

    for (let i = 1; i < fills; i++) {
      console.log(i);
      transition = transition
        .transition()
        .ease(easeLinear)
        .duration(durationPerFill)
        .attr("width", xScale(100))
        .on("end", () => setCurrentColor(colors[i])) // update color on transition end
        .transition()
        .duration(0)
        .attr("width", 0)
        .attr("fill", colors[i + 1]);
    }

    transition
      .transition()
      .ease(easeLinear)
      .duration(durationPerFill)
      .attr("width", xScale(data.precented % 100));
  }, [ ]);

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
        backgroundColor: "#546e7a", // lighter gray background
        borderRadius: 15,
        overflow: "hidden",
        padding: 20,
        color: "#ffffff", // white text
        fontFamily: "Arial", // Arial font
        fontSize: 16, // adjust font size
      }}
    >
      <div style={{ display: "flex" }}>
        <span
          style={{
            padding: 5,
            color: "#ffffff",
            fontFamily: "Arial",
            fontSize: 18,
            fontWeight: "bold",
          }}
        >
          <AnimatedNumber value={data.consumed.toFixed(2)} />
        </span>
        <div
          style={{
            width: width,
            height: height,
            backgroundColor: currentColor,
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
              }}
            />
          </svg>
        </div>
        <span
          style={{
            padding: 5,
            color: "#ffffff",
            fontFamily: "Arial",
            fontSize: 18,
            fontWeight: "bold",
          }}
        >
          {data.max.toFixed(2)}
        </span>
      </div>
      {data.consumed === 0 && (
        <div
          style={{
            color: "#67C240",
            fontSize: 16,
            marginTop: 10,
            lineHeight: "1.5",
            textAlign: "justify",
          }}
        >
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
          {data.type === "Mineral" && (
            <Link to={"/nutrientInfo/mineral/" + data.name}>
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
        <div
          style={{
            color: "#67C240",
            fontSize: 16,
            marginTop: 10,
            lineHeight: "1.5",
            textAlign: "justify",
          }}
        >
          Consumed <AnimatedNumber value={data.consumed.toFixed(2)} />{" "}
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
      {data.type === "Mineral" && data.consumed !== 0 && (
        <div
          style={{
            color: "#67C240",
            fontSize: 16,
            marginTop: 10,
            lineHeight: "1.5",
            textAlign: "justify",
          }}
        >
          Consumed <AnimatedNumber value={data.consumed.toFixed(2)} />
          {data.measurement.match(/\(([^)]+)\)/)[1].toLowerCase()} of{" "}
          {data.name}.
          <Link to={"/nutrientInfo/mineral/" + data.name}>
            {" "}
            <FontAwesomeIcon
              icon={faInfoCircle}
              style={{ fontSize: "24px", marginRight: "5px" }}
            />
          </Link>
        </div>
      )}
      {data.type === "Calories" && data.consumed !== 0 && (
        <div
          style={{
            color: "#67C240",
            fontSize: 16,
            marginTop: 10,
            lineHeight: "1.5",
            textAlign: "justify",
          }}
        >
          Consumed <AnimatedNumber value={data.consumed.toFixed(2)} />{" "}
          {data.measurement} in the {data.name}.
        </div>
      )}
      {(data.type === "Macronutrient" ||
        data.type === "Fat" ||
        data.type === "Carbohydrates") && (
        <div
          style={{
            color: "#67C240",
            fontSize: 16,
            marginTop: 10,
            lineHeight: "1.5",
            textAlign: "justify",
          }}
        >
          Consumed <AnimatedNumber value={data.consumed.toFixed(2)} />
          {data.measurement.match(/\(([^)]+)\)/)[1].toLowerCase()} of{" "}
          {data.name.replace(/([A-Z])/g, " $1").trim()}.
          {data.type === "Macronutrient" ? (
            <Link to={"/nutrientInfo/macronutrient/" + data.name}>
              {" "}
              <FontAwesomeIcon
                icon={faInfoCircle}
                style={{ fontSize: "24px", marginRight: "5px" }}
              />
            </Link>
          ) : (
            <Link
              to={
                "/nutrientInfo/macronutrientTypes/" +
                data.name.replace(/([A-Z])/g, " $1").trim()
              }
            >
              {" "}
              <FontAwesomeIcon
                icon={faInfoCircle}
                style={{ fontSize: "24px", marginRight: "5px" }}
              />
            </Link>
          )}
        </div>
      )}
    </div>
  );
}

export function Gauge2({
  width,
  height,
  diameter,
  legendRectSize,
  legendSpacing,
  fat,
  carbohydrates,
  protein,
}) {
  const svgRef = useRef(null);
  const colors = useMemo(() => ["#6a5071", "#5E74C2", "#C25E74"], []);
  const legendInfo = ["Fat", "Carbohydrates", "Protein"];
  let sections = [fat * 9, carbohydrates * 4, protein * 4];
  const sum = fat * 9 + carbohydrates * 4 + protein * 4;
  if (sections.every((value) => value === 0)) {
    sections = [33.33, 33.33, 33.33];
  }

  useEffect(() => {
    const svg = select(svgRef.current);
    const arcGenerator = arc()
      .innerRadius(0)
      .outerRadius(diameter / 2);

    const pieGenerator = pie()
      .value((d) => d)
      .sort(null);

    const arcs = pieGenerator(sections);

    const path = svg
      .selectAll("path")
      .data(arcs)
      .join("path")
      .attr("fill", (d, i) => colors[i])
      .attr("stroke", "white")
      .attr("stroke-width", 2);

    let previousTransition = null;

    path.each(function (d, i) {
      const totalDuration = 6000; // Total duration of the animation
      const duration = totalDuration * (sections[i] / sum); // Adjust the duration based on the section's percentage of the total

      const dCopy = { ...d }; // Create a copy of d

      const currentTransition = select(this)
        .transition()
        .duration(duration)
        .attrTween("d", function () {
          const i = interpolate(dCopy.startAngle, dCopy.endAngle);
          return function (t) {
            dCopy.endAngle = i(t);
            return arcGenerator(dCopy);
          };
        });
      // Set the stroke-wid

      if (previousTransition) {
        previousTransition.each(() => {
          select(this).transition().delay(duration);
        });
      }

      previousTransition = currentTransition;
    });
  }, [sections, diameter, sum , colors]);

  return (
    <svg ref={svgRef} width={width} height={height}>
      <g transform={`translate(${diameter / 1.9}, ${diameter / 1.9})`}>
        {pie()(sections).map((section, index) => {
          return (
            <g key={index}>
              <path
                d={arc()
                  .innerRadius(0)
                  .outerRadius(diameter / 2)
                  .startAngle(section.startAngle)
                  .endAngle(section.endAngle)()}
              />
              <title>{((section.data / sum) * 100).toFixed(2)}%</title>
            </g>
          );
        })}
      </g>
      <g
        transform={`translate(${diameter + 31}, ${
          diameter / 2 -
          (sections.length * (legendRectSize - 10 + legendSpacing)) / 2
        })`}
      >
        {sections.map((section, index) => {
          return (
            <g
              key={index}
              transform={`translate(0, ${
                index * (legendRectSize + legendSpacing)
              })`}
            >
              <rect
                width={legendRectSize}
                height={legendRectSize}
                fill={colors[index]}
              />
              <text
                x={legendRectSize + legendSpacing}
                y={legendRectSize}
                fill={colors[index]}
              >
                {legendInfo[index]}
              </text>
            </g>
          );
        })}
      </g>
    </svg>
  );
}
export function BarChart({ height, info }) {
  const { data, dataNames } = info;

  console.log(info);
  const width = 3000;
  const margin = { top: 10, right: 20, bottom: 50, left: 40 };

  const chartWidth = width - margin.left - margin.right;
  const chartHeight = height - margin.top - margin.bottom;

  const x = scaleBand().range([0, chartWidth]).padding(0.2).domain(dataNames);

  const y = scaleLinear()
    .range([chartHeight, 0])
    .domain([0, Math.max(...data)]);

  return (
    <div style={{ width: 870, overflowX: "auto" }}>
      <div style={{ minWidth: "max-content" }}>
        <svg width={width} height={height}>
          <g transform={`translate(${margin.left}, ${margin.top})`}>
            <g
              ref={(node) => select(node).call(axisLeft(y))}
              style={{ fontSize: "12px" }}
            />
            <g
              transform={`translate(0, ${chartHeight})`}
              ref={(node) => {
                const axis = axisBottom(x);
                select(node)
                  .call(axis)
                  .selectAll("text")
                  .attr(
                    "transform",
                    (d, i) => `translate(0, ${i % 2 === 0 ? 0 : 15})`
                  );
              }}
              style={{ fontSize: "11px" }}
            />
            {data
              .sort((a, b) => b - a)
              .map((d, i) => (
                <g key={i}>
                  <rect
                    x={x(dataNames[i])} // Change this line
                    y={y(d)}
                    width={x.bandwidth()}
                    height={chartHeight - y(d)}
                    fill="steelblue"
                  />
                  <title>{d}</title>
                </g>
              ))}
          </g>
        </svg>
      </div>
    </div>
  );
}
export function BarChart2({ height, info }) {
  const { data, dataNames, fullData, type } = info;

  console.log(fullData);
  const width = 3000;
  const margin = { top: 10, right: 20, bottom: 40, left: 40 };

  const chartWidth = width - margin.left - margin.right;
  const chartHeight = height - margin.top - margin.bottom;

  const x = scaleBand().range([0, chartWidth]).padding(0.2).domain(dataNames);

  const y = scaleLinear()
    .range([chartHeight, 0])
    .domain([0, Math.max(...data)]);

  return (
    <div style={{ width: 1150, overflowX: "auto" }}>
      <div style={{ minWidth: "max-content" }}>
        <svg width={width} height={height}>
          <g transform={`translate(${margin.left}, ${margin.top})`}>
            <g
              ref={(node) => select(node).call(axisLeft(y))}
              style={{ fontSize: "12px" }}
            />
            <g
              transform={`translate(0, ${chartHeight})`}
              ref={(node) => {
                const axis = axisBottom(x);
                select(node)
                  .call(axis)
                  .selectAll("text")
                  .attr(
                    "transform",
                    (d, i) => `translate(0, ${i % 2 === 0 ? 0 : 15})`
                  );
              }}
              style={{ fontSize: "11px" }}
            />
            {data
              .sort((a, b) => b - a)
              .map((d, i) => (
                <g key={i}>
                  {type === "ImmuneSystem" && (
                    <title>
                      {`Vitamin C: ${fullData[i].C.toFixed(
                        2
                      )}%\nVitamin D: ${fullData[i].D.toFixed(
                        2
                      )}%\nVitamin A: ${fullData[i].A.toFixed(
                        2
                      )}%\nVitamin E: ${fullData[i].E.toFixed(
                        2
                      )}%\nVitamin B6: ${fullData[i].B6.toFixed(
                        2
                      )}%\nVitamin B9: ${fullData[i].B9.toFixed(
                        2
                      )}%\nVitamin B12: ${fullData[i].B12.toFixed(
                        2
                      )}%\nZinc: ${fullData[i].Zinc.toFixed(
                        2
                      )}%\nSelenium : ${fullData[i].Selenium.toFixed(
                        2
                      )}%\nIron: ${fullData[i].Iron.toFixed(
                        2
                      )}%\nProtein: ${fullData[i].Protein.toFixed(
                        2
                      )}g\nFat: ${fullData[i].Fat.toFixed(2)}g\nTotal %: ${d}`}
                    </title>
                  )}
                  {type === "GrowthAndDevelopment" && (
                    <title>
                      {`Vitamin C: ${fullData[i].C.toFixed(
                        2
                      )}%\nVitamin D: ${fullData[i].D.toFixed(
                        2
                      )}%\nVitamin A: ${fullData[i].A.toFixed(
                        2
                      )}%\nVitamin E: ${fullData[i].E.toFixed(
                        2
                      )}%\nVitamin B9: ${fullData[i].B9.toFixed(
                        2
                      )}%\nVitamin B12: ${fullData[i].B12.toFixed(
                        2
                      )}%\nZinc: ${fullData[i].Zinc.toFixed(
                        2
                      )}%\nCalcium : ${fullData[i].Calcium.toFixed(
                        2
                      )}%\nIron: ${fullData[i].Iron.toFixed(
                        2
                      )}%\nProtein: ${fullData[i].Protein.toFixed(
                        2
                      )}g\nFat: ${fullData[i].Fat.toFixed(2)}g\nTotal %: ${d}`}
                    </title>
                  )}
                  {type === "CognitiveFunctionAndBrainHealth" && (
                    <title>
                      {`Vitamin C: ${fullData[i].C.toFixed(
                        2
                      )}%\nVitamin E: ${fullData[i].E.toFixed(
                        2
                      )}%\nVitamin B6: ${fullData[i].B6.toFixed(
                        2
                      )}%\nVitamin B12: ${fullData[i].B12.toFixed(
                        2
                      )}%\nIron: ${fullData[i].Iron.toFixed(
                        2
                      )}%\nFat: ${fullData[i].Fat.toFixed(2)}g\nTotal %: ${d}`}
                    </title>
                  )}
                  {type === "BoneHealth" && (
                    <title>
                      {`Vitamin D: ${fullData[i].D.toFixed(
                        2
                      )}%\nVitamin K: ${fullData[i].K.toFixed(
                        2
                      )}%\nCalcium: ${fullData[i].Calcium.toFixed(
                        2
                      )}%\nPhosphorus: ${fullData[i].Phosphorus.toFixed(
                        2
                      )}%\nMagnesium: ${fullData[i].Magnesium.toFixed(
                        2
                      )}%\nProtein: ${fullData[i].Protein.toFixed(
                        2
                      )}g\nTotal %: ${d}`}
                    </title>
                  )}
                  {type === "PhysicalPerformanceAndFitness" && (
                    <title>
                      {`Vitamin B6: ${fullData[i].B6.toFixed(
                        2
                      )}%\nVitamin B12: ${fullData[i].B12.toFixed(
                        2
                      )}%\nIron: ${fullData[i].Iron.toFixed(
                        2
                      )}%\nPotassium: ${fullData[i].Potassium.toFixed(
                        2
                      )}%\nProtein: ${fullData[i].Protein.toFixed(
                        2
                      )}g\nCarbohydrates: ${fullData[i].Carbohydrates.toFixed(
                        2
                      )}g\nFat: ${fullData[i].Fat.toFixed(2)}g\nTotal %: ${d}`}
                    </title>
                  )}
                  {type === "AgingAndLongevity" && (
                    <title>
                      {`Vitamin C: ${fullData[i].C.toFixed(
                        2
                      )}%\nVitamin E: ${fullData[i].E.toFixed(
                        2
                      )}%\nVitamin D: ${fullData[i].D.toFixed(
                        2
                      )}%\nFiber: ${fullData[i].Fiber.toFixed(
                        2
                      )}%\nProtein: ${fullData[i].Protein.toFixed(
                        2
                      )}g\nTotal %: ${d}`}
                    </title>
                  )}

                  <rect
                    x={x(dataNames[i])}
                    y={y(d)}
                    width={x.bandwidth()}
                    height={chartHeight - y(d)}
                    fill="steelblue"
                  />
                </g>
              ))}
          </g>
        </svg>
      </div>
    </div>
  );
}

export function AnimatedNumber({ value }) {
  const [displayValue, setDisplayValue] = useState(0);

  useEffect(() => {
    const targetValue = parseFloat(value);
    const interval = setInterval(() => {
      setDisplayValue((prevValue) => {
        const newValue = prevValue + targetValue / 100;
        if (newValue < targetValue) {
          return newValue;
        } else {
          clearInterval(interval);
          return targetValue;
        }
      });
    }, 40);

    return () => clearInterval(interval);
  }, [value]);

  return <span>{displayValue.toFixed(2)}</span>;
}

function generateColors(startColor, count) {
  const colors = [startColor];
  for (let i = 1; i < count; i++) {
    const color = chroma(colors[i - 1])
      .darken(0.4)
      .hex();
    colors.push(color);
  }
  return colors;
}
