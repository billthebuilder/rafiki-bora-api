package rafikibora.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Terminal", description = "Terminal details used in administrative or listing operations.")
public class TerminalDto {

    @Schema(description = "Terminal model type", example = "PAX S920")
    String modelType;

    @Schema(description = "Terminal serial number", example = "SN1234567890")
    String serialNo;

    @Schema(description = "Terminal identifier (internal id)", example = "TID-42")
    String id;
}