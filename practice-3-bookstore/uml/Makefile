output_dir := rendered
diagrams := $(patsubst %.uml,$(output_dir)/%.png, $(wildcard *.uml))

render: $(diagrams)

$(output_dir)/%.png: %.uml | $(output_dir)
	plantuml $< -o $(output_dir)

$(output_dir):
	mkdir -p $(output_dir)

clean:
	rm -rf $(output_dir)

.PHONY: render clean
