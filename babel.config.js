const presets = [
  [
    "@babel/env",
    {
      targets: {
        chrome: "51",
      },
    },
  ],
];
const plugins = [["@babel/plugin-transform-exponentiation-operator"]];

module.exports = { presets, plugins };